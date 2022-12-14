-- gennydb.`userstore` definition

CREATE TABLE user_store (
  id bigint NOT NULL AUTO_INCREMENT,
  realm varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  usercode varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  jti_access varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  last_active bigint,
  PRIMARY KEY (id),
  UNIQUE KEY userstore_uniquekey(realm,usercode)
) ENGINE=InnoDB AUTO_INCREMENT=7092 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;