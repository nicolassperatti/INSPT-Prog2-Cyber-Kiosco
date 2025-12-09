-- ============================================
-- SCRIPT DE CREACIÓN: Estructura con Herencia JOINED
-- ============================================
-- Usa este script si vas a crear la BD desde cero

DROP DATABASE IF EXISTS cyberkiosco;
CREATE DATABASE IF NOT EXISTS cyberkiosco;
USE cyberkiosco;

-- Tablas existentes (categoria, marca, producto, etc.)
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
    `descripcion` VARCHAR(255) DEFAULT NULL,
    `id_categoria` INTEGER NOT NULL,
    `id_marca` INTEGER NOT NULL,
    PRIMARY KEY(`id_producto`)
);

CREATE TABLE `carrito` (
    `id_carrito` INTEGER NOT NULL AUTO_INCREMENT,
    `precio_total` DOUBLE DEFAULT NULL,
    `fecha_compra` DATETIME DEFAULT NULL,
    `id_usuario` INTEGER,
    PRIMARY KEY(`id_carrito`)
);

CREATE TABLE `carrito_producto` (
    `id_carrito` INTEGER NOT NULL,
    `id_producto` INTEGER NOT NULL,
    `cantidad_producto` INTEGER NOT NULL,
    `precio_producto` DOUBLE NOT NULL,
    PRIMARY KEY(`id_carrito`, `id_producto`)
);

CREATE TABLE `perfil` (
    `id_perfil` INTEGER NOT NULL AUTO_INCREMENT UNIQUE,
    `nombre_perfil` VARCHAR(255) NOT NULL,
    `foto` VARCHAR(255) NOT NULL,
    `id_usuario` INTEGER NOT NULL UNIQUE,
    PRIMARY KEY(`id_perfil`)
);

-- ============================================
-- NUEVA ESTRUCTURA: Tabla base Usuario
-- ============================================
CREATE TABLE `usuario` (
    `id_usuario` INTEGER NOT NULL AUTO_INCREMENT,
    `nombre` VARCHAR(255) NOT NULL,
    `apellido` VARCHAR(255) NOT NULL,
    `mail` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `tipo_usuario` VARCHAR(20) NOT NULL,  -- Discriminador: 'ADMIN' o 'FINAL'
    PRIMARY KEY(`id_usuario`)
);

-- ============================================
-- NUEVA ESTRUCTURA: Tabla Admin
-- ============================================
CREATE TABLE `admin` (
    `id_usuario` INTEGER NOT NULL PRIMARY KEY,
    FOREIGN KEY (`id_usuario`) REFERENCES `usuario`(`id_usuario`) ON DELETE CASCADE
);

-- ============================================
-- NUEVA ESTRUCTURA: Tabla Final
-- ============================================
CREATE TABLE `final` (
    `id_usuario` INTEGER NOT NULL PRIMARY KEY,
    `fondos` DOUBLE NOT NULL DEFAULT 0,
    FOREIGN KEY (`id_usuario`) REFERENCES `usuario`(`id_usuario`) ON DELETE CASCADE
);

-- ============================================
-- Foreign Keys de otras tablas
-- ============================================
ALTER TABLE `producto`
ADD FOREIGN KEY(`id_categoria`) REFERENCES `categoria`(`id_categoria`) ON DELETE CASCADE,
ADD FOREIGN KEY(`id_marca`) REFERENCES `marca`(`id_marca`) ON DELETE CASCADE;

ALTER TABLE `carrito`
ADD FOREIGN KEY(`id_usuario`) REFERENCES `final`(`id_usuario`) ON DELETE CASCADE;

ALTER TABLE `carrito_producto`
ADD FOREIGN KEY(`id_carrito`) REFERENCES `carrito`(`id_carrito`) ON DELETE CASCADE,
ADD FOREIGN KEY(`id_producto`) REFERENCES `producto`(`id_producto`) ON DELETE CASCADE;

ALTER TABLE `perfil`
ADD FOREIGN KEY(`id_usuario`) REFERENCES `usuario`(`id_usuario`) ON DELETE CASCADE;

-- ============================================
-- Datos de ejemplo
-- ============================================
INSERT INTO `categoria` (`nombre`) VALUES
('Comestible'),
('Bebida');

INSERT INTO `marca` (`nombre`) VALUES
('Saborina'),
('Deliciana'),
('NutriVibe'),
('FrutoDorado');

INSERT INTO `producto` (`nombre`, `stock`, `precio`, `imagen`, `descripcion`, `id_categoria`, `id_marca`) VALUES
('Papas Fritas', 100, 1.99, 'papas_fritas.jpg', 'Deliciosas papas fritas', 1, 1),
('Galletas de Chocolate', 150, 2.49, 'galletas_chocolate.jpg', 'Galletas crujientes con trozos de chocolate', 1, 2),
('Jugo de Naranja', 200, 0.99, 'jugo_de_naranja.jpg', 'Jugo de naranja', 2, 3);

-- ============================================
-- Datos de usuarios con nueva estructura
-- ============================================
-- Insertar Admin
INSERT INTO `usuario` (`nombre`, `apellido`, `mail`, `password`, `tipo_usuario`) VALUES
('adminuser', 'Gómez', 'admin@example.com', '$2a$12$wkp/m0M1paPEccApW7yvI.i1acu3rXaJOuNYlw3IeRAiy7vKR0O8O', 'ADMIN');

-- Insertar en tabla admin
INSERT INTO `admin` (`id_usuario`) VALUES (LAST_INSERT_ID());

-- Insertar Usuarios Finales
INSERT INTO `usuario` (`nombre`, `apellido`, `mail`, `password`, `tipo_usuario`) VALUES
('Jose', 'Pérez', 'jose.perez@example.com', '$2a$12$/Ajy9di1bvKeytY7yAel5ev4GtSoPMp.cfYBqQOFxF1T5tmjNNBHG', 'FINAL'),
('Maria', 'López', 'maria.lopez@example.com', '$2a$12$qsDnp8ALqDebdfGofdqM1.q38raqWTymhsByv8baY6n5n7xC0CKIO', 'FINAL');

-- Insertar en tabla final con fondos
INSERT INTO `final` (`id_usuario`, `fondos`) VALUES
(2, 500.00),
(3, 200.00);

-- Insertar más usuarios finales
INSERT INTO `usuario` (`nombre`, `apellido`, `mail`, `password`, `tipo_usuario`) VALUES
('Carlos', 'Ruiz', 'carlos.ruiz@example.com', 'carlos123', 'FINAL'),
('Ana', 'Martínez', 'ana.martinez@example.com', 'ana123', 'FINAL');

INSERT INTO `final` (`id_usuario`, `fondos`) VALUES
(4, 300.00),
(5, 150.00);

-- Insertar perfiles
INSERT INTO `perfil` (`nombre_perfil`, `foto`, `id_usuario`) VALUES
('Administrador Principal', 'admin_foto.jpg', 1),
('Perfil de José', 'jose_foto.jpg', 2),
('Perfil de María', 'maria_foto.jpg', 3);