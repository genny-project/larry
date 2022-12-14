-- gennydb.`userstore` definition

CREATE TABLE userstore (
  id bigint NOT NULL AUTO_INCREMENT,
  realm varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  usercode varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  jtiaccess varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  timestamp bigint,
  PRIMARY KEY (id),
  UNIQUE KEY userstore_uniquekey (usercode,realm)
) ENGINE=InnoDB AUTO_INCREMENT=7092 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;