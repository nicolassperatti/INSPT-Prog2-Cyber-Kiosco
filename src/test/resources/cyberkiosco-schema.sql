-- CREATE DATABASE IF NOT EXISTS cyberkiosco;
-- USE cyberkiosco;


CREATE TABLE `categoria` (
	`id_categoria` INTEGER NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(50) NOT NULL UNIQUE,
	PRIMARY KEY(`id_categoria`)
);


CREATE TABLE `marca` (
	`id_marca` INTEGER NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL UNIQUE,
	PRIMARY KEY(`id_marca`)
);


CREATE TABLE `producto` (
	`id_producto` INTEGER NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(50) NOT NULL,
	`stock` INTEGER NOT NULL,
	`precio` DOUBLE NOT NULL,
	`imagen` VARCHAR(255) DEFAULT NULL,
	`descripcion` VARCHAR(255) DEFAULT NULL,     -- modificacion para que acepte valores nulos
	`id_categoria` INTEGER NOT NULL,
	`id_marca` INTEGER NOT NULL,
	PRIMARY KEY(`id_producto`)
);


CREATE TABLE `carrito` (
	`id_carrito` INTEGER NOT NULL AUTO_INCREMENT,
	`precio_total` DOUBLE NOT NULL,
	`fecha_compra` DATETIME NOT NULL,
	`id_usuario` INTEGER,       -- provisoriamente puede ser null
	PRIMARY KEY(`id_carrito`)
);


CREATE TABLE `carrito_producto` (
	`id_carrito` INTEGER NOT NULL,
	`id_producto` INTEGER NOT NULL,
	`cantidad_producto` INTEGER NOT NULL,
	`precio_producto` DOUBLE NOT NULL,
	PRIMARY KEY(`id_carrito`, `id_producto`)
);


CREATE TABLE `rol` (
        `id_rol` INTEGER NOT NULL AUTO_INCREMENT,
        `nombre` VARCHAR(255) NOT NULL,
        PRIMARY KEY(`id_rol`)
);


CREATE TABLE `perfil` (
	`id_perfil` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
	`nombre_perfil` VARCHAR(255) NOT NULL,
	`foto` VARCHAR(255) NOT NULL,
	`id_usuario` INTEGER NOT NULL UNIQUE,
	PRIMARY KEY(`id_perfil`)
);


CREATE TABLE `usuario` (
	`id_usuario` INTEGER NOT NULL AUTO_INCREMENT,
	`nombre` VARCHAR(255) NOT NULL UNIQUE,
	`apellido` VARCHAR(255) NOT NULL,
	`mail` VARCHAR(255) NOT NULL,
	`password` VARCHAR(255) NOT NULL,
	`fondos` DOUBLE NOT NULL DEFAULT 0,
	`id_rol` INTEGER NOT NULL,
	PRIMARY KEY(`id_usuario`)
);


ALTER TABLE `producto`
ADD FOREIGN KEY(`id_categoria`) REFERENCES `categoria`(`id_categoria`)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `producto`
ADD FOREIGN KEY(`id_marca`) REFERENCES `marca`(`id_marca`)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `carrito_producto`
ADD FOREIGN KEY(`id_carrito`) REFERENCES `carrito`(`id_carrito`)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `carrito_producto`
ADD FOREIGN KEY(`id_producto`) REFERENCES `producto`(`id_producto`)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `carrito`
ADD FOREIGN KEY(`id_usuario`) REFERENCES `usuario`(`id_usuario`)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `usuario`
ADD FOREIGN KEY(`id_rol`) REFERENCES `rol`(`id_rol`)
ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE `perfil`
ADD FOREIGN KEY(`id_usuario`) REFERENCES `usuario`(`id_usuario`)
ON UPDATE CASCADE ON DELETE CASCADE;

