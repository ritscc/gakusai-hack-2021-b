CREATE TABLE IF NOT EXISTS `user_gift` (
  `user_id` INT UNSIGNED NOT NULL,
  `gift_id` INT UNSIGNED NOT NULL,
  `quantity` INT UNSIGNED NOT NULL,
  `type` INT UNSIGNED NOT NULL,
  `received_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `received_by` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`user_id`, `gift_id`),
  INDEX `fk_user_gift_gift_id_idx` (`gift_id` ASC) VISIBLE,
  CONSTRAINT `fk_user_gift_user_id`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_gift_gift_id`
    FOREIGN KEY (`gift_id`)
    REFERENCES `gift` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;
