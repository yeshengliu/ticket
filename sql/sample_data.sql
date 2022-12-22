USE ticket;
SET NAMES utf8mb4;

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ticket_activity
-- ----------------------------
DROP TABLE IF EXISTS `ticket_activity`;

CREATE TABLE
  `ticket_activity` (
    `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'Ticket Activity ID',
    `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'Ticket Activity Name',
    `ticket_id` bigint NOT NULL,
    `original_price` decimal(10, 2) NOT NULL COMMENT 'Original Price',
    `sale_price` decimal(10, 2) NOT NULL COMMENT 'Sale Price',
    `activity_status` int NOT NULL DEFAULT 0 COMMENT 'Ticket Activity Status, 0:Sold Out 1:On Sale',
    `start_time` datetime(0) NULL DEFAULT NULL COMMENT 'Activity Start Time',
    `end_time` datetime(0) NULL DEFAULT NULL COMMENT 'Activity End Time',
    `total_stock` bigint UNSIGNED NOT NULL COMMENT 'Ticket Total Units',
    `available_stock` int NOT NULL COMMENT 'Ticket Available Units',
    `lock_stock` bigint UNSIGNED NOT NULL DEFAULT 0 COMMENT 'Units in Lock',
    PRIMARY KEY (`id`) USING BTREE,
    INDEX `id` (`id`, `name`, `ticket_id`) USING BTREE,
    INDEX `name` (`name`) USING BTREE
  ) ENGINE = InnoDB AUTO_INCREMENT = 27 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket_activity
-- ----------------------------
INSERT INTO `ticket_activity` VALUES (1, 'Test1', 999, 2.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (2, 'Test2', 999, 3.88, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (3, 'Test3', 999, 8.99, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (4, 'Test4', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (5, 'Test5', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (6, 'Test6', 999, 0.00, 99.00, 0, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (7, 'Test7', 999, 0.00, 99.00, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (8, 'Test8', 999, 0.00, 99.00, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (9, 'Test9', 999, 99.99, 88.88, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (10, 'Test10', 999, 850.00, 850.00, 1, '2020-11-01 19:21:20', NULL, 0, 0, 0);
INSERT INTO `ticket_activity` VALUES (11, 'Test11', 999, 850.00, 850.00, 1, '2020-11-01 19:21:20', '2020-11-20 16:50:40', 10, 0, 0);
INSERT INTO `ticket_activity` VALUES (12, 'Test12', 999, 850.00, 850.00, 1, '2020-11-01 19:21:20', '2020-11-18 16:50:33', 10, 0, 0);
INSERT INTO `ticket_activity` VALUES (13, 'Test13', 1001, 320.00, 320.00, 1, '2020-11-05 08:39:24', '2020-11-05 08:39:24', 10, 9, 0);

-- ----------------------------
-- Table structure for ticket
-- ----------------------------
DROP TABLE IF EXISTS `ticket`;
CREATE TABLE `ticket` (
  `id` bigint UNSIGNED NOT NULL AUTO_INCREMENT,
  `ticket_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ticket_desc` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `ticket_price` decimal(10, 2) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1002 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of ticket
-- ----------------------------
INSERT INTO `ticket` VALUES (11, '11', '11', 100.00);
INSERT INTO `ticket` VALUES (12, '11', '11', 100.00);
INSERT INTO `ticket` VALUES (999, 'JJ Lin Class A', 'JJ Lin Concert in Seattle Class A', 850.00);
INSERT INTO `ticket` VALUES (1001, 'JJ Lin Class C', 'JJ Lin Concert in Seattle Class C', 320.00);