package io.github.lancelothuxi.mock.agent.functions;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompoundVariable implements Function {


    private static final FunctionParser functionParser = new FunctionParser();
    private static final Map<String, Class<? extends Function>> functions = new HashMap<>();

    static {
        try {
            String str = CompoundVariable.class.getPackage().getName() + ".impl";
            List<String> classes = ClassFinder.getClasssFromPackage(str);
            for (String clazzName : classes) {
                Function tempFunc = Class.forName(clazzName)
                        .asSubclass(Function.class)
                        .getDeclaredConstructor().newInstance();
                String referenceKey = tempFunc.getReferenceKey();
                if (referenceKey.length() > 0) { // ignore self
                    functions.put(referenceKey, tempFunc.getClass());
                }
            }
            if (functions.isEmpty()) {
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String rawParameters;
    private ArrayList<Object> compiledComponents = new ArrayList<>();

    public static Object getNamedFunction(String functionName) throws Exception {
        if (functions.containsKey(functionName)) {
            try {
                return functions.get(functionName).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new Exception(e);
            }
        }
        functionName = "${" + functionName + "}";

        return functionName;
    }

    public void setParameters(String parameters) {
        this.rawParameters = parameters;
        if (parameters == null || parameters.length() == 0) {
            return;
        }

        try {
            compiledComponents = functionParser.compileString(parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getReferenceKey() {
        return "";
    }

    @Override
    public void setParameters(Collection<CompoundVariable> parameters) throws Exception {

    }


    @Override
    public String execute(Object... args) {
        if (compiledComponents == null || compiledComponents.isEmpty()) {
            return "";
        }

        StringBuilder results = new StringBuilder();
        for (Object item : compiledComponents) {
            if (item instanceof Function) {
                try {
                    results.append(((Function) item).execute(args));
                } catch (Exception e) {
                }
            } else {
                results.append(item);
            }
        }
        return results.toString();
    }

}
