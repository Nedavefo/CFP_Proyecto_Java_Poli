package edu.poli.modulo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;


public class GenerateInfoFiles {

    // Configuración general

    /** Carpeta base donde se crean los archivos de salida. */
    private static final String BASE_DIR = "./data";

    /** Cantidad por defecto de registros a generar (puedes ajustar libremente). */
    private static final int DEFAULT_PRODUCTS = 50;
    private static final int DEFAULT_SALESMEN = 12;

    /** Rango de líneas de venta por vendedor. */
    private static final int MIN_SALES_LINES_PER_SELLER = 5;
    private static final int MAX_SALES_LINES_PER_SELLER = 25;

    /** Cantidades de producto por línea (exactamente 3 como pide el enunciado). */
    private static final int PRODUCTS_PER_SALE_LINE = 3;
    private static final int MIN_QTY = 1;
    private static final int MAX_QTY = 9;

    /** Semilla para obtener datos reproducibles. Cambia el valor para variar los datos. */
    private static final long SEED = 20250907L;

    /** Formateador de precios a dos decimales. */
    private static final DecimalFormat PRICE_FMT = new DecimalFormat("0.00");

    static {
        PRICE_FMT.setRoundingMode(RoundingMode.HALF_UP);
        Locale.setDefault(new Locale("es", "CO"));
    }

    // Catálogos base (nombres/ apellidos / productos)

    private static final List<String> DOC_TYPES = Arrays.asList("CC", "CE", "TI", "PP");
    private static final List<String> FIRST_NAMES = Arrays.asList(
            "Juan", "Camila", "Sofía", "Mateo", "Valentina", "Andrés", "Daniela",
            "Santiago", "Laura", "Nicolás", "María", "Samuel", "Isabella", "David",
            "Sebastián", "Gabriela", "Ana María", "Carlos", "Paula", "Lucía"
    );

    private static final List<String> LAST_NAMES = Arrays.asList(
            "García", "Rodríguez", "Martínez", "López", "Hernández", "González",
            "Pérez", "Sánchez", "Ramírez", "Torres", "Flores", "Rivera", "Gómez",
            "Díaz", "Vargas", "Jiménez", "Rojas", "Moreno", "Castro", "Romero"
    );

    // Unas raíces para nombres de productos; el ID será P001, P002, ...
    private static final List<String> PRODUCT_BASE_NAMES = Arrays.asList(
            "Taladro Inalámbrico", "Lijadora Orbital", "Amoladora Angular", "Martillo Demoledor",
            "Destornillador Eléctrico", "Sierra Caladora", "Sierra Circular", "Multímetro Digital",
            "Juego de Brocas", "Llave de Impacto", "Pistola de Calor", "Soldador Eléctrico",
            "Linterna LED", "Cinta Métrica", "Nivel Láser", "Compresor Portátil",
            "Hidrolavadora", "Pistola de Pintura", "Motosierra", "Pulidora"
    );

    // Punto de entrada

    /**
     * Método principal. Orquesta la generación de archivos.
     * No solicita información al usuario.
     */
    public static void main(String[] args) {
        System.out.println("== GENERACION DE ARCHIVOS DE PRUEBA ==");
        System.out.println("Inicio: " + LocalDateTime.now());

        try {
            ensureBaseDir();

            Random rng = new Random(SEED);

            // 1) Generar productos y guardar la lista en memoria para usarlos en ventas
            List<Product> products = createProductsFile(DEFAULT_PRODUCTS, rng);

            // 2) Generar info de vendedores y obtener la lista de vendedores creada
            List<Salesman> salesmen = createSalesManInfoFile(DEFAULT_SALESMEN, rng);

            // 3) Por cada vendedor, generar su archivo de ventas (líneas pseudoaleatorias)
            for (Salesman sm : salesmen) {
                int salesLines = rng.nextInt(MAX_SALES_LINES_PER_SELLER - MIN_SALES_LINES_PER_SELLER + 1)
                        + MIN_SALES_LINES_PER_SELLER;
                createSalesMenFile(salesLines, sm.getFullName(), sm.documentNumber, sm.documentType, products, rng);
            }

            System.out.println("Archivos generados en: " + new File(BASE_DIR).getCanonicalPath());
            System.out.println("Generación finalizada exitosamente.");
        } catch (Exception ex) {
            System.err.println("Ocurrió un error durante la generación de archivos:");
            ex.printStackTrace();
        }

        System.out.println("Fin: " + LocalDateTime.now());
    }

    // Métodos solicitados (a, b, c) 

    /**
     * b) Crea un archivo con información pseudoaleatoria de productos.
     *
     * @param productsCount cantidad de productos a generar
     * @param rng           generador pseudoaleatorio
     * @return lista de productos generados en memoria
     * @throws IOException si falla la escritura
     */
    public static List<Product> createProductsFile(int productsCount, Random rng) throws IOException {
        List<Product> products = new ArrayList<>(productsCount);

        File out = new File(BASE_DIR, "productos.txt");
        try (BufferedWriter bw = newBufferedWriter(out)) {
            for (int i = 1; i <= productsCount; i++) {
                String id = String.format("P%03d", i);
                String name = randomProductName(rng);
                double price = randomPrice(rng, 10.0, 500.0); // precios entre 10 y 500
                products.add(new Product(id, name, price));

                // Formato: IDProducto;NombreProducto;PrecioPorUnidad
                bw.write(id + ";" + name + ";" + PRICE_FMT.format(price));
                bw.newLine();
            }
        }

        return products;
    }

    /**
     * c) Crea un archivo con información pseudoaleatoria de vendedores.
     *
     * @param salesmanCount cantidad de vendedores
     * @param rng           generador pseudoaleatorio
     * @return lista de vendedores generados en memoria
     * @throws IOException si falla la escritura
     */
    public static List<Salesman> createSalesManInfoFile(int salesmanCount, Random rng) throws IOException {
        List<Salesman> salesmen = new ArrayList<>(salesmanCount);

        File out = new File(BASE_DIR, "vendedores_info.txt");
        try (BufferedWriter bw = newBufferedWriter(out)) {
            for (int i = 0; i < salesmanCount; i++) {
                String docType = DOC_TYPES.get(rng.nextInt(DOC_TYPES.size()));
                String docNumber = randomDocNumber(rng);
                String firstName = FIRST_NAMES.get(rng.nextInt(FIRST_NAMES.size()));
                String lastName = LAST_NAMES.get(rng.nextInt(LAST_NAMES.size()))
                        + " " + LAST_NAMES.get(rng.nextInt(LAST_NAMES.size()));

                Salesman sm = new Salesman(docType, docNumber, firstName, lastName);
                salesmen.add(sm);

                // Formato: TipoDocumento;NumeroDocumento;Nombres;Apellidos
                bw.write(docType + ";" + docNumber + ";" + firstName + ";" + lastName);
                bw.newLine();
            }
        }

        return salesmen;
    }

    /**
     * a) Dada una cantidad, un nombre y un id, crea un archivo pseudoaleatorio de ventas
     * de un vendedor con el nombre y el id dados.
     * <p>
     * En este proyecto extendemos la firma para incluir el tipo de documento y la lista de
     * productos disponibles, de forma que las ventas hagan referencia a IDs válidos.
     *
     * @param randomSalesCount cantidad de líneas de venta a generar
     * @param name             nombre completo del vendedor (solo informativo en este método)
     * @param id               número de documento del vendedor
     * @param docType          tipo de documento del vendedor
     * @param products         catálogo de productos válidos
     * @param rng              generador pseudoaleatorio
     * @throws IOException si falla la escritura
     */
    public static void createSalesMenFile(int randomSalesCount,
                                          String name,
                                          String id,
                                          String docType,
                                          List<Product> products,
                                          Random rng) throws IOException {

        // Nombre de archivo: ventas_{Tipo}_{Numero}.txt  (un archivo por vendedor)
        String fileName = "ventas_" + docType + "_" + id + ".txt";
        File out = new File(BASE_DIR, fileName);

        try (BufferedWriter bw = newBufferedWriter(out)) {
            for (int i = 0; i < randomSalesCount; i++) {
                // Elegimos 3 productos (pueden repetirse entre líneas, no dentro de la misma línea)
                List<Product> picked = pickDistinctProducts(products, PRODUCTS_PER_SALE_LINE, rng);

                // Armamos cantidades para cada producto
                StringBuilder line = new StringBuilder();
                // Encabezado de la línea con identificación del vendedor
                line.append(docType).append(";").append(id);

                for (Product p : picked) {
                    int qty = rng.nextInt(MAX_QTY - MIN_QTY + 1) + MIN_QTY;
                    line.append(";").append(p.id).append(";").append(qty);
                }

                bw.write(line.toString());
                bw.newLine();
            }
        }
    }

    // Utilidades internas

    private static void ensureBaseDir() throws IOException {
        File base = new File(BASE_DIR);
        if (!base.exists()) {
            Files.createDirectories(base.toPath());
        }
    }

    private static BufferedWriter newBufferedWriter(File file) throws IOException {
        // Aseguramos UTF-8 y sobrescritura limpia
        return new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8, false));
    }

    private static String randomProductName(Random rng) {
        String base = PRODUCT_BASE_NAMES.get(rng.nextInt(PRODUCT_BASE_NAMES.size()));
        // Pequeña variación para nombres si se excede la lista base
        if (rng.nextDouble() < 0.25) {
            String suf = rng.nextBoolean() ? " Pro" : " Plus";
            return base + suf;
        }
        return base;
    }

    private static double randomPrice(Random rng, double min, double max) {
        double v = min + (max - min) * rng.nextDouble();
        // Redondeo a 2 decimales con HALF_UP mediante DecimalFormat al escribir
        return v;
    }

    private static String randomDocNumber(Random rng) {
        // 10 dígitos pseudoaleatorios, con prefijo para evitar ceros a la izquierda
        long base = 1000000000L + Math.abs(rng.nextLong() % 9000000000L);
        return Long.toString(base);
    }

    private static List<Product> pickDistinctProducts(List<Product> universe, int k, Random rng) {
        List<Product> copy = new ArrayList<>(universe);
        List<Product> picked = new ArrayList<>(k);
        for (int i = 0; i < k && !copy.isEmpty(); i++) {
            int idx = rng.nextInt(copy.size());
            picked.add(copy.remove(idx));
        }
        return picked;
    }

    // Modelos simples (para claridad)

    /** Representa un producto disponible. */
    public static class Product {
        public final String id;
        public final String name;
        public final double unitPrice;

        public Product(String id, String name, double unitPrice) {
            this.id = id;
            this.name = name;
            this.unitPrice = unitPrice;
        }
    }

    /** Representa un vendedor. */
    public static class Salesman {
        public final String documentType;
        public final String documentNumber;
        public final String firstName;
        public final String lastName;

        public Salesman(String documentType, String documentNumber, String firstName, String lastName) {
            this.documentType = documentType;
            this.documentNumber = documentNumber;
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
}
