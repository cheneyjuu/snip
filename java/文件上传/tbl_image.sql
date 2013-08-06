CREATE TABLE `tbl_image` (
  `id` bigint(11) NOT NULL AUTO_INCREMENT,
  `content_id` int(11) DEFAULT NULL,
  `image_name` varchar(100) DEFAULT NULL,
  `image_path` varchar(200) DEFAULT NULL,
  `middle_name` varchar(100) DEFAULT NULL,
  `big_name` varchar(100) DEFAULT NULL,
  `small_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);