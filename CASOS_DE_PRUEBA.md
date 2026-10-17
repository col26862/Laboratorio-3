# Casos de prueba

| Caso | Entrada | Resultado esperado | Resultado obtenido |
|---|---|---|---|
| Diagnostico metropolitano | 1 hora, zona metropolitana | Q150.00 y 1 dia | Q150.00 y 1 dia |
| Diagnostico periferico | 1 hora, zona periferica | Q225.00 y 2 dias | Q225.00 y 2 dias |
| Horas por puntos | Instalacion metro, 2.5 h y 5 puntos | 3.75 h por puntos, 4 facturables, Q460.00 | Q460.00 |
| Fraccion de hora | Instalacion con 3.2 horas dominantes | 4 horas facturables | 4 horas facturables |
| Certificado y garantia | Costo base Q460 y materiales Q2,000 | Q680.00 | Q680.00 |
| Mantenimiento | 2.5 h, 12,000 BTU, metro | Q455.00 y 2 dias | Q455.00 y 2 dias |
| Diagnostico invalido | 2 horas | Rechazo sin registrar | Rechazado |
| Aire periferico | Zona periferica | Rechazo sin registrar | Rechazado |
| Certificacion | Aire asignado a cuadrilla no certificada | Rechazo | Rechazado |
| Capacidad | Orden 1.5 h en cuadrilla de 1 h | Rechazo | Rechazado |
| Visitas diagnostico | Tres visitas fallidas | Estado CANCELADA | CANCELADA |
| Ingreso por visitas | Segunda y tercera visitas | Q80.00 | Q80.00 |
| Datos iniciales | Ocho ordenes precargadas | Q0.00 de ingresos | Q0.00 |
| Cotizacion | Cotizar una instalacion valida | Q0.00 de ingresos | Q0.00 |
| Retiro | Asignar y retirar antes de iniciar | Orden pendiente y Q0.00 | Orden pendiente y Q0.00 |
| Operacion rechazada | Intentar reasignar una cancelada | Sin cambio de ingresos | Sin cambio |

Los resultados se verifican automaticamente ejecutando la clase `tecnihogar.Pruebas`.
