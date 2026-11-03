# Sales Module – Test Data Generator (Java)

Java project for the *Object-Oriented Programming* course at **Politécnico Grancolombiano**.

The `GenerateInfoFiles` class creates pseudo-random flat files that later feed a sales-reporting system:

| File | Contents |
|---|---|
| `productos.txt` | Product ID, name and price |
| `vendedores_info.txt` | Salesperson document type, document number, first and last name |
| `ventas_{DocType}_{Number}.txt` | One file per salesperson with the products they sold and quantities |

Generation uses a **fixed seed**, so every run produces the same data set and results are reproducible. The defaults are 50 products and 12 salespeople, with 5 to 25 sale lines each.

## Project structure

```
src/edu/poli/modulo/GenerateInfoFiles.java   # Data generator
data/                                        # Generated files (sample output committed)
```

## Requirements

- Java JDK 8 or newer

## Run

```bash
javac -d bin src/edu/poli/modulo/GenerateInfoFiles.java
java -cp bin edu.poli.modulo.GenerateInfoFiles
```

Expected console output:

```
== GENERACION DE ARCHIVOS DE PRUEBA ==
Archivos generados en: .../data
Generación finalizada exitosamente.
```

## Status

- [x] Delivery 1: test-data generator (`GenerateInfoFiles`)
- [ ] Delivery 2: report generation (`main` class)
