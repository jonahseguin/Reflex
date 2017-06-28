/*
 * Copyright (c) Jonah Seguin (Shawckz) 2017.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.jonahseguin.reflex.backend.database.mongo;

import com.google.common.primitives.Primitives;
import com.jonahseguin.reflex.Reflex;
import com.jonahseguin.reflex.backend.configuration.AbstractSerializer;
import com.jonahseguin.reflex.backend.database.DBManager;
import com.jonahseguin.reflex.backend.database.mongo.annotations.CollectionName;
import com.jonahseguin.reflex.backend.database.mongo.annotations.DatabaseSerializer;
import com.jonahseguin.reflex.backend.database.mongo.annotations.MongoColumn;
import com.jonahseguin.reflex.util.exception.AbstractSerializerException;
import com.jonahseguin.reflex.util.exception.AutoMongoError;
import com.jonahseguin.reflex.util.exception.AutoMongoException;
import com.jonahseguin.reflex.util.serial.RDatabaseSerializer;
import com.jonahseguin.reflex.util.serial.ReflexSerializer;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.apache.commons.lang.ClassUtils;
import org.bson.Document;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public abstract class AutoMongo {

    public static AutoMongo fromDocument(Class<? extends AutoMongo> type, Document document) throws AutoMongoError {
        try {
            AutoMongo mongo = type.newInstance();
            for (Field field : mongo.getClass().getDeclaredFields()) {
                MongoColumn mongoColumn = field.getAnnotation(MongoColumn.class);
                if (mongoColumn != null) {
                    Object value = document.get(mongoColumn.name());
                    if (value != null) {
                        try {
                            mongo.setValue(value, field.getType(), field, type);
                        } catch (AutoMongoException ex) {
                            Reflex.getReflexLogger().error("AutoMongo: Could not set value (fromDocument)", ex);
                            throw new AutoMongoError("Could not set value '" + value.toString() + "' for field '" + field.getName() + "'");
                        }
                    }
                }
            }

            for (Field field : type.getSuperclass().getDeclaredFields()) {
                MongoColumn mongoColumn = field.getAnnotation(MongoColumn.class);
                if (mongoColumn != null) {
                    Object value = document.get(mongoColumn.name());
                    if (value != null) {
                        try {
                            mongo.setValue(value, field.getType(), field, type);
                        } catch (AutoMongoException ex) {
                            Reflex.getReflexLogger().error("AutoMongo: Could not set value (fromDocument)", ex);
                            throw new AutoMongoError("Could not set value '" + value.toString() + "' for field '" + field.getName() + "'");
                        }
                    }
                }
            }
            return mongo;
        } catch (InstantiationException | IllegalAccessException ex) {
            Reflex.getReflexLogger().error("AutoMongo: Could not instantiate/access (fromDocument)", ex);
        }
        return null;
    }

    public static List<AutoMongo> select(Document search, Class<? extends AutoMongo> type) throws AutoMongoError {
        List<AutoMongo> vals = new ArrayList<>();
        CollectionName collectionName = type.getAnnotation(CollectionName.class);
        if (collectionName == null) {
            throw new AutoMongoError("Collection Name annotation has not been added for AutoMongo class: " + type.getSimpleName());
        }

        MongoCollection<Document> col = DBManager.getDb().getCollection(collectionName.name());

        for (Document doc : col.find(search)) {
            try {
                AutoMongo mongo = fromDocument(type, doc);
                vals.add(mongo);
            } catch (AutoMongoError error) {
                Reflex.getReflexLogger().error("AutoMongo: Could not load AutoMongo from document (select)", error);
                throw error;
            }

        }
        return vals;
    }

    public static AutoMongo selectOne(Document search, Class<? extends AutoMongo> type) throws AutoMongoError {
        CollectionName collectionName = type.getAnnotation(CollectionName.class);
        if (collectionName == null) {
            throw new AutoMongoError("Collection Name annotation has not been added for AutoMongo class: " + type.getSimpleName());
        }

        MongoCollection<Document> col = DBManager.getDb().getCollection(collectionName.name());

        MongoCursor<Document> cursor = col.find(search).iterator();

        if (cursor.hasNext()) {
            Document doc = cursor.next();
            try {
                return fromDocument(type, doc);
            } catch (AutoMongoError error) {
                Reflex.getReflexLogger().error("AutoMongo: Could not load AutoMongo from document (selectOne)", error);
                throw error;
            }
        }
        return null;
    }

    public Document toDocument() {
        HashMap<String, Object> values = new HashMap<>();

        putValues(values, this.getClass());
        putValues(values, this.getClass().getSuperclass());
        return new Document(values);
    }

    private void putValues(Map<String, Object> values, Class type) {
        for (Field field : type.getDeclaredFields()) {
            field.setAccessible(true);
            MongoColumn column = field.getAnnotation(MongoColumn.class);
            if (column != null) {
                try {
                    values.put(column.name(), getValue(field));
                } catch (AutoMongoException ex) {
                    Reflex.getReflexLogger().error("AutoMongo: Could not get value for field (putValues)", ex);
                    throw new AutoMongoError("Could not get value for field '" + field.getName() + "': " + ex.getMessage(), ex);
                }
            }
        }
    }

    public void update() throws AutoMongoError {
        if (!this.getClass().isAnnotationPresent(CollectionName.class)) {
            throw new AutoMongoError("Collection Name annotation has not been added for AutoMongo class: " + this.getClass().getSimpleName());
        }
        String tableName = this.getClass().getAnnotation(CollectionName.class).name();

        MongoCollection<Document> col = DBManager.getDb().getCollection(tableName);

        String identifier = null;
        Object identifierValue = null;
        HashMap<String, Object> values = new HashMap<>();

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            MongoColumn column = field.getAnnotation(MongoColumn.class);
            if (column != null) {
                if (column.identifier()) {
                    identifier = column.name();
                    try {
                        identifierValue = getValue(field);
                    } catch (AutoMongoException ex) {
                        Reflex.getReflexLogger().error("AutoMongo: Could not get identifier value for field (update)", ex);
                        throw new AutoMongoError("Could not get identifier value: " + ex.getMessage(), ex);
                    }
                } else {
                    try {
                        values.put(column.name(), getValue(field));
                    } catch (AutoMongoException ex) {
                        Reflex.getReflexLogger().error("AutoMongo: Could not get value for field (put values) (update)", ex);
                        throw new AutoMongoError("Could not get value for field '" + field.getName() + "': " + ex.getMessage(), ex);
                    }
                }
            }
        }

        //Superclass
        for (Field field : this.getClass().getSuperclass().getDeclaredFields()) {
            field.setAccessible(true);
            MongoColumn column = field.getAnnotation(MongoColumn.class);
            if (column != null) {
                if (column.identifier()) {
                    identifier = column.name();
                    try {
                        identifierValue = getValue(field);
                    } catch (AutoMongoException ex) {
                        Reflex.getReflexLogger().error("AutoMongo: Could not get identifier value for field (update)", ex);
                        throw new AutoMongoError("Could not get identifier value: " + ex.getMessage(), ex);
                    }
                } else {
                    try {
                        values.put(column.name(), getValue(field));
                    } catch (AutoMongoException ex) {
                        Reflex.getReflexLogger().error("AutoMongo: Could not get value for field (put values) (update)", ex);
                        throw new AutoMongoError("Could not get value for field '" + field.getName() + "': " + ex.getMessage(), ex);
                    }
                }
            }
        }

        if (identifier == null) {
            Reflex.getReflexLogger().error("AutoMongo: Identifier not found for class: " + this.getClass().getSimpleName() + " (update)");
            throw new AutoMongoError("Identifier not found for AutoMongo class: " + this.getClass().getSimpleName());
        }
        Document doc = new Document(identifier, identifierValue);
        Document searchQuery = new Document().append(identifier, identifierValue);

        doc.putAll(values);

        if (documentExists(searchQuery, col)) {
            col.updateOne(searchQuery, new Document("$set", doc));
        } else {
            col.insertOne(doc);
        }
    }

    public void delete() throws AutoMongoError {
        if (!this.getClass().isAnnotationPresent(CollectionName.class)) {
            throw new AutoMongoError("Collection Name annotation has not been added for AutoMongo class: " + this.getClass().getSimpleName());
        }
        String tableName = this.getClass().getAnnotation(CollectionName.class).name();

        MongoCollection<Document> col = DBManager.getDb().getCollection(tableName);

        String identifier = null;
        Object identifierValue = null;

        for (Field field : this.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            MongoColumn column = field.getAnnotation(MongoColumn.class);
            if (column != null) {
                if (column.identifier()) {
                    identifier = column.name();
                    try {
                        identifierValue = getValue(field);
                    } catch (AutoMongoException ex) {
                        Reflex.getReflexLogger().error("AutoMongo: Could not get identifier value (delete)", ex);
                        throw new AutoMongoError("Could not get identifier value: " + ex.getMessage(), ex);
                    }
                }
            }
        }
        Document searchQuery = new Document().append(identifier, identifierValue);
        col.deleteOne(searchQuery);
    }

    private boolean documentExists(Document search, MongoCollection col) {
        return col.find(search).limit(1).iterator().hasNext();
    }

    public String getValue(Field field) throws AutoMongoException {
        try {
            Object o = field.get(this);
            if (o == null) {
                o = "NULL";
            }
            String ret = o.toString();
            if (field.isAnnotationPresent(DatabaseSerializer.class)) {
                DatabaseSerializer serializer = field.getAnnotation(DatabaseSerializer.class);
                ret = serializer.serializer().newInstance().toString(o);
            } else if (field.isAnnotationPresent(RDatabaseSerializer.class)) {
                ReflexSerializer serializer = ((RDatabaseSerializer) field.getAnnotation(RDatabaseSerializer.class)).serializer().newInstance();
                ret = serializer.toString(o);
            }
            return ret;
        } catch (IllegalAccessException ex) {
            throw new AutoMongoException("Could not access field/type when getting value", ex);
        } catch (InstantiationException ex) {
            throw new AutoMongoException("Could not instantiate serializer", ex);
        }
    }

    private void setValue(Object value, Class<?> type, Field field, Class<? extends AutoMongo> mongoType) throws AutoMongoException {
        try {
            if (type.isPrimitive()) {
                type = ClassUtils.primitiveToWrapper(type);
            }
            field.setAccessible(true);
            if (value == null || type.equals(value.getClass())) {
                field.set(this, value);
            } else if (field.isAnnotationPresent(DatabaseSerializer.class)) {
                AbstractSerializer serializer = field.getAnnotation(DatabaseSerializer.class).serializer().newInstance();
                try {
                    field.set(this, serializer.fromString(value));
                } catch (AbstractSerializerException ex) {
                    throw new AutoMongoException("Could not set field using DatabaseSerializer", ex);
                }
            } else if (field.isAnnotationPresent(RDatabaseSerializer.class)) {
                ReflexSerializer serializer = ((RDatabaseSerializer) field.getAnnotation(RDatabaseSerializer.class)).serializer().newInstance();
                field.set(this, serializer.fromString(value, mongoType));
            } else if (type.equals(UUID.class)) {
                field.set(this, type.getDeclaredMethod("fromString", String.class).invoke(null, value.toString()));
            } else if (!Primitives.isWrapperType(type) && !type.equals(String.class) && !type.equals(Long.class) && !type.isPrimitive()) {
                field.set(this, type.getDeclaredMethod("valueOf", String.class).invoke(null, value.toString()));
            } else {
                field.set(this, type.getDeclaredMethod("valueOf", value.getClass()).invoke(null, value.toString()));
            }
        } catch (NoSuchMethodException ex) {
            throw new AutoMongoException("Could not find method", ex);
        } catch (InstantiationException ex) {
            throw new AutoMongoException("Could not instantiate serializer", ex);
        } catch (IllegalAccessException ex) {
            throw new AutoMongoException("Could not access field/type", ex);
        } catch (InvocationTargetException ex) {
            throw new AutoMongoException("Invocation of target error", ex);
        }
    }
}