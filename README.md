# Java
*.class
*.jar
*.war
*.ear

# VS Code
.vscode/
.settings/
.classpath
.project

# Eclipse
bin/
tmp/

# Sistema operativo
.DS_Store
Thumbs.db

# Archivos generados
/data/
📄 Archivo: README.md
También en la raíz de tu proyecto.

markdown
Copy code
# Proyecto Módulo - Entrega 1
**Institución:** Politécnico Grancolombiano  
**Curso:** Programación Orientada a Objetos (Java)  
**Entrega 1 (Semana 3):** Clase `GenerateInfoFiles`

---

## 📌 Descripción
Este proyecto genera **archivos de prueba pseudoaleatorios** para el sistema de ventas.  
La clase principal `GenerateInfoFiles` se encarga de crear:

- `productos.txt` → Lista de productos con ID, nombre y precio.  
- `vendedores_info.txt` → Lista de vendedores con tipo y número de documento, nombres y apellidos.  
- `ventas_{TipoDoc}_{Numero}.txt` → Un archivo de ventas por cada vendedor, con el detalle de productos vendidos.  

Los archivos se guardan en la carpeta `data/` en la raíz del proyecto.

---

## 📂 Estructura del proyecto
VentasModulo/
├─ src/
│ └─ edu/
│ └─ poli/
│ └─ modulo/
│ └─ GenerateInfoFiles.java
├─ bin/ # Archivos compilados .class
├─ data/ # Archivos generados automáticamente
├─ .gitignore
└─ README.md

---

## ⚙️ Requisitos
- **Java JDK 8 (1.8)**  
- **VS Code** con la extensión *Extension Pack for Java*  
  (o Eclipse IDE for Java Developers).  

---

## ▶️ Ejecución

### 
1. Compilar
```bash
javac -d bin src/edu/poli/modulo/GenerateInfoFiles.java
2. Ejecutar
bash
Copy code
java -cp bin edu.poli.modulo.GenerateInfoFiles
3. Resultado esperado
En consola:

bash
Copy code
== GENERACION DE ARCHIVOS DE PRUEBA ==
Archivos generados en: .../VentasModulo/data
Generación finalizada exitosamente.
En la carpeta data/:

productos.txt

vendedores_info.txt

ventas_CC_XXXXXXXX.txt (un archivo por vendedor)

✅ Estado de la entrega
 Clase GenerateInfoFiles implementada

 Archivos planos generados correctamente

 Clase main (se desarrollará en la Entrega 2)

👨‍💻 Autor
Tu Nombre
Politécnico Grancolombiano

---

## 🚀 Comandos para subir a GitHub

Copia y ejecuta en la terminal desde la carpeta raíz del proyecto:

```bash
# 1. Inicializa git
git init

# 2. Añade todo al staging
git add .

# 3. Haz el primer commit
git commit -m "Entrega 1 - GenerateInfoFiles"

# 4. Crea la rama main
git branch -M main

# 5. Conecta con el repositorio en GitHub
git remote add origin https://github.com/TUUSUARIO/VentasModulo.git

# 6. Sube el proyecto
git push -u origin main
⚠️ Reemplaza https://github.com/TUUSUARIO/VentasModulo.git con la URL de tu propio repositorio en GitHub.

