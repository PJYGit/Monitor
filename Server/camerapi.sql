/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 80016
 Source Host           : localhost:3306
 Source Schema         : camerapi

 Target Server Type    : MySQL
 Target Server Version : 80016
 File Encoding         : 65001

 Date: 08/07/2019 17:26:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address
-- ----------------------------
DROP TABLE IF EXISTS `address`;
CREATE TABLE `address`  (
  `uid` int(10) NOT NULL,
  `addr` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address
-- ----------------------------
INSERT INTO `address` VALUES (1, '666');
INSERT INTO `address` VALUES (222, '666');

-- ----------------------------
-- Table structure for device
-- ----------------------------
DROP TABLE IF EXISTS `device`;
CREATE TABLE `device`  (
  `mac` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `devid` int(10) NOT NULL AUTO_INCREMENT,
  `state` int(10) NOT NULL,
  `ip` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `time` bigint(20) NOT NULL,
  `lastbeat` bigint(20) NOT NULL,
  PRIMARY KEY (`devid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of device
-- ----------------------------
INSERT INTO `device` VALUES ('', 1, 2, '192.168.43.157', 0, 233);

-- ----------------------------
-- Table structure for login
-- ----------------------------
DROP TABLE IF EXISTS `login`;
CREATE TABLE `login`  (
  `uid` int(10) NOT NULL,
  `token` char(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `deadtime` bigint(20) NULL DEFAULT NULL,
  `logintime` bigint(20) NULL DEFAULT NULL,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of login
-- ----------------------------
INSERT INTO `login` VALUES (1, '5a2aca28e25b40cdb6e596d63b96205b', 1562556293, 1562552681);
INSERT INTO `login` VALUES (2, 'd2960a64816d5f9b0497abd44ca831cf', 1562398745, 2);
INSERT INTO `login` VALUES (233, '233', 666, 3);
INSERT INTO `login` VALUES (668, '', 1562471902, 111);
INSERT INTO `login` VALUES (669, '', 1562471804, 111);
INSERT INTO `login` VALUES (670, '', 1562501223, NULL);
INSERT INTO `login` VALUES (671, '777', 666, NULL);
INSERT INTO `login` VALUES (672, '', 0, NULL);
INSERT INTO `login` VALUES (673, 'fc307bbbfb00ef09dd9c06838efa292a', 1562510338, NULL);
INSERT INTO `login` VALUES (674, '', 0, 1562510656);
INSERT INTO `login` VALUES (675, '', 0, 0);
INSERT INTO `login` VALUES (676, '', 0, 0);

-- ----------------------------
-- Table structure for pictures
-- ----------------------------
DROP TABLE IF EXISTS `pictures`;
CREATE TABLE `pictures`  (
  `pid` int(10) NOT NULL AUTO_INCREMENT,
  `pname` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `type` int(10) NOT NULL,
  `devid` int(10) NOT NULL,
  `time` bigint(20) NOT NULL,
  PRIMARY KEY (`pid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `uid` int(10) NOT NULL AUTO_INCREMENT,
  `username` text CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` char(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `salt` char(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `group` int(10) NOT NULL,
  `regtime` bigint(20) NOT NULL,
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 677 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'bd8cc18c102ffb970e2acaa49ae5f823', 'd66e3a96eb08095bc774d0515ad547c4', 1, 1562406827);
INSERT INTO `user` VALUES (2, 'naive', '4b1c12ab2f82b6d9e970d127e640b7d2', 'e10adc3949ba59abbe56e057f20f883e', 0, 1562416827);
INSERT INTO `user` VALUES (672, 'zxt', 'e2684a63588372ae4677e6810d396393', '8d56d94f8527bb08769976e80ecc9876', 0, 1562505778);
INSERT INTO `user` VALUES (673, 'dsy', '4dedbfffe4183baecee9c7336ef31440', 'a6d97d00049d68f59eb50b62d7a1456c', 0, 1562505797);
INSERT INTO `user` VALUES (674, 'lgc', 'c59daf4114ed73d49ec837281b46948c', '900682b5afdb2fe70bb22b5f653a018b', 0, 1562510656);
INSERT INTO `user` VALUES (675, 'mz', '875f214bd3e9aa836cc948b1c7f2eecc', '532718a89331179ba4fc45abc3c74e6f', 0, 1562511214);
INSERT INTO `user` VALUES (676, 'pjy', 'c4e17a6e684c1b1712eab96148e70577', '3181e843fc0293550ce7bd7ce7bba8d3', 0, 1562511224);

SET FOREIGN_KEY_CHECKS = 1;
