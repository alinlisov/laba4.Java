package lab4;
public class TestModule implements ExecutableModule<String> {
    @Override
    public String execute() {
        return "Всі зміни працюють на льоту! Версія 2.0";
    }
    @Override
    public String getVersion() {
        return "v1.0-generics";
    }
    @Override
    public String toString() { return getVersion() + ": " + execute(); }
}