package lab4;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
public class ModuleManager<R, T extends ExecutableModule<R>> {
    private final List<T> activeModules = new ArrayList<>();
    public void registerModuleNames(Collection<? extends String> moduleNames) {
        System.out.println("[ModuleManager] Реєстрація модулів з джерела:");
        for (String name : moduleNames) { System.out.println(" - Модуль у черзі: " + name); }
    }
    public void addModule(T module) {
        activeModules.add(module);
    }
    /** ЗАСТОСУВАННЯ WILDCARD  */
    public void exportResults(Collection<? super R> outputContainer) {
        for (T module : activeModules) {
            R result = module.execute();
            outputContainer.add(result);
        }
    }
}