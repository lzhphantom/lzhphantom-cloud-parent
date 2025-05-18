package com.lzhphantom.common.web.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.lzhphantom.common.web.utils.UserHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Objects;

public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        if (Objects.isNull(metaObject)) {
            throw new IllegalArgumentException("MetaObject cannot be null");
        } else {
            this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
            this.strictInsertFill(metaObject, "createdAt", LocalDateTime::now, LocalDateTime.class);
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
            this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime::now, LocalDateTime.class);
            this.strictInsertFill(metaObject, "createUserId", UserHandler::getCurrentUserId, Long.class);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        if (Objects.isNull(metaObject)) {
            throw new IllegalArgumentException("MetaObject cannot be null");
        } else {
            this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
            this.strictUpdateFill(metaObject, "updatedAt", LocalDateTime::now, LocalDateTime.class);
        }
    }
}
