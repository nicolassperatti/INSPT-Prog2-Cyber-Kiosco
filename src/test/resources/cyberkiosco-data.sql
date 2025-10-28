INSERT INTO `categoria` (`nombre`) VALUES
('snack'),
('bebida')
;

INSERT INTO `marca` (`nombre`) VALUES
('Saborina'),
('Deliciana'),
('NutriVibe'),
('FrutoDorado')
;

INSERT INTO `producto` (`nombre`, `stock`, `precio`, `imagen`, `descripcion`, `id_categoria`, `id_marca`) VALUES
('Papas Fritas', 100, 1.99, 'papas_fritas.png', 'Deliciosas papas fritas', 1, 1),
('Galletas de Chocolate', 150, 2.49, 'galletas_chocolate.png', 'Galletas crujientes con trozos de chocolate', 1, 2),
('Jugo de Naranja', 200, 0.99, 'jugo_de_naranja.png', 'Jugo de naranja', 2, 3),
('Juguo de Manzana', 120, 1.49, 'jugo_de_manzana.png', 'Jugo natural de manzana, refrescante y saludable', 2, 4),
('Queso', 80, 1.79, 'queso.png', 'Queso fesco', 1, 1),
('Agua Mineral', 300, 0.89, 'agua_mineral.png', 'Botella de agua mineral sin gas', 2, 2),
('Barra de Cereal', 50, 2.29, 'barra_cereal.png', 'Barra energética de avena y frutas', 1, 3),
('Gaseosa Cola', 180, 1.29, 'gaseosa_cola.png', 'Refresco cola, sabor clásico', 2, 1),
('Chicles Mentolados', 200, 0.79, 'chicles_mentolados.png', 'Chicles con sabor refrescante a menta', 1, 4),
('Café Instantáneo', 60, 2.99, 'cafe_instantaneo.png', 'Café soluble para preparar rápidamente', 2, 3),
('Pebete', 150, 1.49, 'pebete.png', 'Pebete con jamon y queso', 1, 2),
('Leche', 100, 1.59, 'leche.png', 'Leche con calcio y proteinas', 2, 4),
('Nueces', 50, 2.99, 'nueces.png', 'Nueces nutritivas', 1, 1),
('Limonada Natural', 120, 1.19, 'limonada_natural.png', 'Limonada fresca con sabor natural', 2, 2),
('Pancho', 90, 1.99, 'pancho.png', 'Salchichas de pollo con pan suave', 1, 3)
;

INSERT INTO rol (nombre) VALUES 
('Administrador'),
('Usuario')
;


INSERT INTO usuario (nombre, apellido, mail, password, fondos, id_rol) VALUES
('adminuser', 'Gómez', 'admin@example.com', 'admin123', 1000.00, 1),
('Jose', 'Pérez', 'jose.perez@example.com', 'jose123', 500.00, 2),
('Maria', 'López', 'maria.lopez@example.com', 'maria123', 200.00, 2),
('Carlos', 'Ruiz', 'carlos.ruiz@example.com', 'carlos123', 300.00, 2),
('Ana', 'Martínez', 'ana.martinez@example.com', 'ana123', 150.00, 2),
('Luis', 'Fernández', 'luis.fernandez@example.com', 'luis123', 400.00, 2),
('Sofia', 'Ramírez', 'sofia.ramirez@example.com', 'sofia123', 250.00, 2)
;


INSERT INTO perfil (nombre_perfil, foto, id_usuario) VALUES
('Administrador Principal', 'admin_foto.jpg', 1),
('Perfil de José', 'jose_foto.jpg', 2),
('Perfil de María', 'maria_foto.jpg', 3),
('Perfil de Carlos', 'carlos_foto.jpg', 4),
('Perfil de Ana', 'ana_foto.jpg', 5),
('Perfil de Luis', 'luis_foto.jpg', 6),
('Perfil de Sofía', 'sofia_foto.jpg', 7)
;


-- Carrito 1
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (7.76, '2025-10-01 14:30:00', 2);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(1, 1, 2, 1.99),
(1, 3, 1, 0.99),
(1, 5, 1, 1.79)
;

-- Carrito 2
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (5.56, '2025-10-03 10:15:00', 3);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(2, 2, 2, 2.49),
(2, 6, 1, 0.89)
;

-- Carrito 3
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (9.06, '2025-10-05 17:50:00', 4);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(3, 4, 2, 1.49),
(3, 7, 1, 2.29),
(3, 10, 1, 2.99)
;

-- Carrito 4
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (6.85, '2025-10-07 13:45:00', 5);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(4, 11, 2, 1.49),
(4, 14, 1, 1.19),
(4, 9, 1, 0.79);

-- Carrito 5
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (4.37, '2025-10-08 09:20:00', 6);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(5, 6, 2, 0.89),
(5, 3, 1, 0.99),
(5, 9, 1, 0.79)
;

-- Carrito 6
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (8.56, '2025-10-10 18:00:00', 7);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(6, 15, 2, 1.99),
(6, 1, 1, 1.99),
(6, 6, 1, 0.89)
;

-- Carrito 7
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (7.77, '2025-10-11 12:05:00', 2);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(7, 13, 1, 2.99),
(7, 5, 2, 1.79),
(7, 8, 1, 1.29)
;

-- Carrito 8
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (6.77, '2025-10-12 20:30:00', 3);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(8, 12, 1, 1.59),
(8, 11, 2, 1.49),
(8, 9, 1, 0.79),
(8, 6, 1, 0.89)
;

-- Carrito 9
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (5.87, '2025-10-14 08:40:00', 4);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(9, 7, 1, 2.29),
(9, 3, 1, 0.99),
(9, 14, 2, 1.19),
(9, 6, 1, 0.89)
;

-- Carrito 10
INSERT INTO carrito (precio_total, fecha_compra, id_usuario) VALUES (4.67, '2025-10-15 16:10:00', 5);
INSERT INTO carrito_producto (id_carrito, id_producto, cantidad_producto, precio_producto) VALUES
(10, 2, 1, 2.49),
(10, 9, 2, 0.79),
(10, 6, 1, 0.89)
;