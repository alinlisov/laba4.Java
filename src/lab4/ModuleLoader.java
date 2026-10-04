package lab4;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
public class ModuleLoader<T extends ExecutableModule<?>> extends ClassLoader {
    private final String classPath;
    private final Class<T> targetInterface;
    public ModuleLoader(String classPath, Class<T> targetInterface) {
        super(ClassLoader.getSystemClassLoader());
        this.classPath = classPath;
        this.targetInterface = targetInterface; }
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        // Завантажуємо з тимчасової папки ТІЛЬКИ сам TestModule.
        // Усі інші класи (включаючи ExecutableModule) завантажуються через системний ClassLoader.
        if (name.equals("lab4.TestModule")) { return findClass(name); }
        return super.loadClass(name);}
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        File classFile = new File(classPath, name.replace('.', '/') + ".class");
        if (!classFile.exists()) {
            throw new ClassNotFoundException("Файл класу не знайдено: " + classFile.getAbsolutePath());}
        try {
            byte[] bytes = Files.readAllBytes(classFile.toPath());
            Class<?> loadedClass = defineClass(name, bytes, 0, bytes.length);

            if (!targetInterface.isAssignableFrom(loadedClass)) {
                throw new ClassCastException("Завантажений клас " + name + " не реалізує " + targetInterface.getName());}
            return loadedClass;
        } catch (IOException e) {
            throw new ClassNotFoundException("Не вдалося прочитати файл класу", e); }
    }
    @SuppressWarnings("unchecked")
    public Class<? extends T> loadModuleClass(String className) throws ClassNotFoundException {
        return (Class<? extends T>) loadClass(className);
    }
}