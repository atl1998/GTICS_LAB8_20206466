SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema lab8gtics
-- -----------------------------------------------------

CREATE SCHEMA IF NOT EXISTS `lab8gtics` DEFAULT CHARACTER SET utf8 ;
USE `lab8gtics` ;

-- -----------------------------------------------------
-- Table `lab8gtics`.`evento`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lab8gtics`.`evento` (
  `idevento` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NULL,
  `fecha` DATE NULL,
  `categoria` ENUM('Conferencia', 'Exposicion', 'Taller', 'Concierto') NULL,
  `capacidadmaxima` INT NULL,
  `reservasactuales` INT NULL,
  PRIMARY KEY (`idevento`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `lab8gtics`.`reserva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lab8gtics`.`reserva` (
  `idreserva` INT NOT NULL AUTO_INCREMENT,
  `nombrepersona` VARCHAR(45) NULL,
  `correopersona` VARCHAR(45) NULL,
  `numerocupos` INT NULL,
  `idevento` INT NOT NULL,
  PRIMARY KEY (`idreserva`),
  INDEX `fk_reserva_evento_idx` (`idevento` ASC) VISIBLE,
  CONSTRAINT `fk_reserva_evento`
    FOREIGN KEY (`idevento`)
    REFERENCES `lab8gtics`.`evento` (`idevento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `lab8gtics`.`reserva`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `lab8gtics`.`reserva` (
  `idreserva` INT NOT NULL AUTO_INCREMENT,
  `nombrepersona` VARCHAR(45) NULL,
  `correopersona` VARCHAR(45) NULL,
  `numerocupos` INT NULL,
  `idevento` INT NOT NULL,
  PRIMARY KEY (`idreserva`),
  INDEX `fk_reserva_evento_idx` (`idevento` ASC) VISIBLE,
  CONSTRAINT `fk_reserva_evento`
    FOREIGN KEY (`idevento`)
    REFERENCES `mydb`.`evento` (`idevento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

-- Añadir datos en la tabla `evento`
INSERT INTO lab8gtics.evento (idevento, nombre, fecha, categoria, capacidadmaxima, reservasactuales)
VALUES 
    (1, 'Conferencia de Tecnología', '2024-11-20', 'Conferencia', 200, 2),
    (2, 'Exposición de Arte', '2024-12-01', 'Exposicion', 100, 4),
    (3, 'Taller de Fotografía', '2024-12-10', 'Taller', 50, 8),
    (4, 'Concierto de Rock', '2024-12-15', 'Concierto', 500, 10);

-- Añadir datos en la tabla `reserva`
INSERT INTO lab8gtics.reserva (idreserva, nombrepersona, correopersona, numerocupos, idevento)
VALUES 
    (1,'Juan Perez', 'juan.perez@example.com', 2, 1),
    (2,'Maria Lopez', 'maria.lopez@example.com', 1, 2),
    (3,'Carlos Sánchez', 'carlos.sanchez@example.com', 2, 2),
    (4,'Lucia Martinez', 'lucia.martinez@example.com', 1, 2),
    (5,'Ana Gonzalez', 'ana.gonzalez@example.com', 4, 3),
    (6,'Roberto Diaz', 'roberto.diaz@example.com', 2, 3),
    (7,'Laura Torres', 'laura.torres@example.com', 2, 3),
    (8,'Miguel Jimenez', 'miguel.jimenez@example.com', 5, 4),
    (9,'Sofia Fernandez', 'sofia.fernandez@example.com', 3, 4),
    (10,'Luis Garcia', 'luis.garcia@example.com', 2, 4);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
