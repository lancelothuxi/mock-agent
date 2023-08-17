package io.github.lancelothuxi.mock.agent.functions;


import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class FunctionParser {

    public ArrayList<Object> compileString(String value) throws Exception {
        StringReader reader = new StringReader(value);
        ArrayList<Object> result = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        char previous = ' '; // TODO - why use space?
        char[] current = new char[1];
        try {
            while (reader.read(current) == 1) {
                if (current[0] == '\\') { // Handle escapes
                    previous = current[0];
                    if (reader.read(current) == 0) {
                        break;
                    }
                    // Keep '\' unless it is one of the escapable chars '$' ',' or '\'
                    // N.B. This method is used to parse function parameters, so must treat ',' as special
                    if (current[0] != '$' && current[0] != ',' && current[0] != '\\') {
                        buffer.append(previous); // i.e. '\\'
                    }
                    previous = ' ';
                    buffer.append(current[0]);
                } else if (current[0] == '{' && previous == '$') {// found "${"
                    buffer.deleteCharAt(buffer.length() - 1);
                    if (buffer.length() > 0) {// save leading text
                        result.add(buffer.toString());
                        buffer.setLength(0);
                    }
                    result.add(makeFunction(reader));
                    previous = ' ';
                } else {
                    buffer.append(current[0]);
                    previous = current[0];
                }
            }

            if (buffer.length() > 0) {
                result.add(buffer.toString());
            }
        } catch (IOException e) {
            result.clear();
            result.add(value);
        }
        if (result.isEmpty()) {
            result.add("");
        }
        return result;
    }

    Object makeFunction(StringReader reader) throws Exception {
        char[] current = new char[1];
        char previous = ' '; // TODO - why use space?
        StringBuilder buffer = new StringBuilder();
        Object function;
        try {
            while (reader.read(current) == 1) {
                if (current[0] == '\\') {
                    if (reader.read(current) == 0) {
                        break;
                    }
                    previous = ' ';
                    buffer.append(current[0]);
                } else if (current[0] == '(' && previous != ' ') {
                    String funcName = buffer.toString();
                    function = CompoundVariable.getNamedFunction(funcName.trim());
                    if (function instanceof Function) {
                        ((Function) function).setParameters(parseParams(reader));
                        if (firstNonSpace(reader, '#') != '}') {
                            reader.reset();// set to start of string
                            char[] cb = new char[100];
                            int nbRead = reader.read(cb);
                            throw new Exception("Expected } after "
                                    + funcName + " function call in " + new String(cb, 0, nbRead));
                        }

                        return function;
                    } else { // Function does not exist, so treat as per missing variable
                        buffer.append(current[0]);
                    }
                } else if (current[0] == '}') {// variable, or function with no parameter list
                    function = CompoundVariable.getNamedFunction(buffer.toString());
                    if (function instanceof Function) {// ensure that setParameters() is called.
                        ((Function) function).setParameters(new ArrayList<CompoundVariable>());
                    }
                    buffer.setLength(0);
                    return function;
                } else {
                    buffer.append(current[0]);
                    previous = current[0];
                }
            }
        } catch (IOException e) {
            return null;
        }
        return buffer.toString();
    }

    private char firstNonSpace(StringReader reader, char defaultResult) throws IOException {
        char[] current = new char[1];
        while (reader.read(current) == 1) {
            if (!Character.isSpaceChar(current[0])) {
                return current[0];
            }
        }
        return defaultResult;
    }

    ArrayList<CompoundVariable> parseParams(StringReader reader) throws Exception {
        ArrayList<CompoundVariable> result = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        char[] current = new char[1];
        char previous = ' ';
        int functionRecursion = 0;
        int parenRecursion = 0;
        try {
            while (reader.read(current) == 1) {
                if (current[0] == '\\') { // Process escaped characters
                    buffer.append(current[0]); // Store the \
                    if (reader.read(current) == 0) {
                        break; // end of buffer
                    }
                    previous = ' ';
                    buffer.append(current[0]); // store the following character
                } else if (current[0] == ',' && functionRecursion == 0) {
                    CompoundVariable param = new CompoundVariable();
                    param.setParameters(buffer.toString());
                    buffer.setLength(0);
                    result.add(param);
                } else if (current[0] == ')' && functionRecursion == 0 && parenRecursion == 0) {
                    // Detect functionName() so this does not generate empty string as the parameter
                    if (buffer.length() == 0 && result.isEmpty()) {
                        return result;
                    }
                    // Normal exit occurs here
                    CompoundVariable param = new CompoundVariable();
                    param.setParameters(buffer.toString());
                    buffer.setLength(0);
                    result.add(param);
                    return result;
                } else if (current[0] == '{' && previous == '$') {
                    buffer.append(current[0]);
                    previous = current[0];
                    functionRecursion++;
                } else if (current[0] == '}' && functionRecursion > 0) {
                    buffer.append(current[0]);
                    previous = current[0];
                    functionRecursion--;
                } else if (current[0] == ')' && functionRecursion == 0 && parenRecursion > 0) {
                    buffer.append(current[0]);
                    previous = current[0];
                    parenRecursion--;
                } else if (current[0] == '(' && functionRecursion == 0) {
                    buffer.append(current[0]);
                    previous = current[0];
                    parenRecursion++;
                } else {
                    buffer.append(current[0]);
                    previous = current[0];
                }
            }
        } catch (IOException e) {// Should not happen with StringReader
        }
        // Dropped out, i.e. did not find closing ')'
        CompoundVariable var = new CompoundVariable();
        var.setParameters(buffer.toString());
        result.add(var);
        return result;
    }
}
