/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.reflex.check.inspect;

import com.shawckz.reflex.util.obj.Alert;

public enum RInspectResultType {

    PASSED, FAILED;

    public Alert.Type translateToAlertType() {
        if (this == PASSED) {
            return Alert.Type.INSPECT_PASS;
        }
        else {
            return Alert.Type.INSPECT_FAIL;
        }
    }

}
