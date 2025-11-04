USE cyberkiosco;

SELECT  usuario.id_usuario, carrito.id_carrito, carrito.fecha_compra, producto.nombre, carrito_producto.cantidad_producto
FROM carrito_producto
INNER JOIN carrito
ON carrito.id_carrito = carrito_producto.id_carrito
INNER JOIN usuario
ON usuario.id_usuario = carrito.id_usuario
INNER JOIN producto
ON carrito_producto.id_producto = producto.id_producto
WHERE usuario.id_usuario = 1;


SELECT carrito.id_carrito, carrito.fecha_compra, carrito.precio_total FROM carrito;

