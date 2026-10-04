package lab4;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
public class Main {
    public static void main(String[] args) throws Exception {
        File tempDir = Files.createTempDirectory("custom_classes_lab4").toFile();
        tempDir.deleteOnExit();
        // визначає шлях до TestModule.java
        Path userDir = Paths.get(System.getProperty("user.dir"));
        File javaFile = userDir.resolve("src").resolve("lab4").resolve("TestModule.java").toFile();
        System.out.println("[DEBUG] Перевірка файлу за шляхом: " + javaFile.getAbsolutePath());
        System.out.println("[DEBUG] Файл існує? " + javaFile.exists());

        if (!javaFile.exists()) {
            System.err.println("ПОМИЛКА: Файл TestModule.java не знайдено за вказаним шляхом!");
            System.err.println("Перевірте, чи файл знаходиться в папці src/lab4/TestModule.java");
            return;}

        String className = "lab4.TestModule";
        long lastModified = javaFile.lastModified(); // Беремо початковий час модифікації
        // 1. Демонстрація Wildcard (? extends String)
        List<String> initialModules = List.of("lab4.TestModule");
        ModuleManager<String, ExecutableModule<String>> manager = new ModuleManager<>();
        manager.registerModuleNames(initialModules);

        System.out.println("\n[INFO] Програма запущена.");
        System.out.println("[INFO] Змініть рядок у TestModule.java та збережіть файл (Ctrl+S");

        while (true) {
            try {
                // Перевіряємо, чи змінився час останнього редагування
                if (javaFile.lastModified() > lastModified) {
                    lastModified = javaFile.lastModified();
                    System.out.println("\n[!] Виявлено зміни у файлі " + javaFile.getName());

                    boolean compiled = recompile(javaFile.getAbsolutePath(), tempDir.getAbsolutePath());
                    if (compiled) {
                        @SuppressWarnings({"rawtypes", "unchecked"})
                        ModuleLoader<ExecutableModule<String>> loader =
                                new ModuleLoader<>(tempDir.getAbsolutePath(), (Class) ExecutableModule.class);

                        Class<? extends ExecutableModule<String>> moduleClass = loader.loadModuleClass(className);
                        ExecutableModule<String> moduleInstance = moduleClass.getDeclaredConstructor().newInstance();

                        System.out.println(" Успішно завантажено модуль версії: " + moduleInstance.getVersion());
                        System.out.println(" Результат виконання: " + moduleInstance.execute());
                        // 2. Демонстрація Wildcard (? super String)
                        List<Object> resultsCollector = new ArrayList<>();
                        manager.addModule(moduleInstance);
                        manager.exportResults(resultsCollector);

                        System.out.println(" Експортовано результатів у List<Object>: " + resultsCollector.size());
                    } else {
                        System.out.println(" [ERROR] Помилка компіляції!");
                    }
                }
                Thread.sleep(1500);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private static boolean recompile(String javaFilePath, String outputDirPath) {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            System.err.println("Помилка: Не знайдено JavaCompiler. Переконайтеся, що проєкт запускається під JDK.");
            return false;
        }
        int result = compiler.run(null, null, null, "-d", outputDirPath, javaFilePath);
        return result == 0;
    }
}