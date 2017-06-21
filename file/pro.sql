/*
Navicat MySQL Data Transfer
Source Host     : localhost:3306
Source Database : savemap
Target Host     : localhost:3306
Target Database : savemap
Date: 2017-03-21 21:29:19
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for pro
-- ----------------------------
DROP TABLE IF EXISTS `pro`;
CREATE TABLE `pro` (
  `RID` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`RID`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pro
-- ----------------------------
INSERT INTO `pro` VALUES ('1', '北京市');
INSERT INTO `pro` VALUES ('2', '天津市');
INSERT INTO `pro` VALUES ('3', '河北省');
INSERT INTO `pro` VALUES ('4', '山西省');
INSERT INTO `pro` VALUES ('5', '辽宁省');
INSERT INTO `pro` VALUES ('6', '吉林省');
INSERT INTO `pro` VALUES ('7', '黑龙江省');
INSERT INTO `pro` VALUES ('8', '上海市');
INSERT INTO `pro` VALUES ('9', '江苏省');
INSERT INTO `pro` VALUES ('10', '浙江省');
INSERT INTO `pro` VALUES ('11', '安徽省');
INSERT INTO `pro` VALUES ('12', '福建省');
INSERT INTO `pro` VALUES ('13', '江西省');
INSERT INTO `pro` VALUES ('14', '山东省');
INSERT INTO `pro` VALUES ('15', '河南省');
INSERT INTO `pro` VALUES ('16', '湖北省');
INSERT INTO `pro` VALUES ('17', '湖南省');
INSERT INTO `pro` VALUES ('18', '广东省');
INSERT INTO `pro` VALUES ('19', '广西壮族自治区');
INSERT INTO `pro` VALUES ('20', '海南省');
INSERT INTO `pro` VALUES ('21', '重庆市');
INSERT INTO `pro` VALUES ('22', '四川省');
INSERT INTO `pro` VALUES ('23', '贵州省');
INSERT INTO `pro` VALUES ('24', '云南省');
INSERT INTO `pro` VALUES ('25', '陕西省');
INSERT INTO `pro` VALUES ('26', '甘肃省');
INSERT INTO `pro` VALUES ('27', '青海省');
INSERT INTO `pro` VALUES ('28', '宁夏回族自治区');
