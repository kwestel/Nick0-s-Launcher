import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

public class JRVME_Helper
{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Find Classes

    public static Class findClassByName(String originalClassName) throws Exception
    {
        Class[] loadedClasses = JRVME_ClassScope.getLoadedClasses(JRVME_System.minecraftClassLoader);
        for ( Class actualClass : loadedClasses )
        {
            if ( actualClass.getName().equals(originalClassName) ) { return actualClass; }
        }
        return null;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Find Methods

    public static Method findClassMethod(Class classToParse, String methodToFind, boolean onlyStatic) throws Exception
    {
        Method[] methodList = classToParse.getDeclaredMethods();
        for ( Method actualMethod : methodList )
        {
            if ( onlyStatic && !isStatic(actualMethod) ) { continue; }
            if ( actualMethod.getName().trim().equals(methodToFind.trim()) ) { return actualMethod; }
        }
        return null;
    }

    public static Method[] findClassMethods(Class classToParse, String methodToFind, boolean onlyStatic) throws Exception
    {
        ArrayList<Method> methodsFound = new ArrayList<Method>();

        Method[] methodList = classToParse.getDeclaredMethods();
        for ( Method actualMethod : methodList )
        {
            if ( onlyStatic && !isStatic(actualMethod) ) { continue; }
            if ( actualMethod.getName().trim().equals(methodToFind.trim()) ) { methodsFound.add(actualMethod); }
        }
        return methodsFound.toArray(new Method[methodsFound.size()]);
    }

    public static Method[] listClassMethods(Class classToParse, boolean onlyStatic) throws Exception
    {
        Method[] methodList = classToParse.getDeclaredMethods();
        ArrayList<Method> classMethods = new ArrayList<Method>();

        for ( Method actualMethod : methodList )
        {
            if ( onlyStatic && !isStatic(actualMethod) ) { continue; }
            classMethods.add(actualMethod);
        }

        return classMethods.toArray(new Method[classMethods.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Find Fields

    public static Field findClassField(Class classToParse, String fieldToFind, boolean onlyStatic) throws Exception
    {
        Field[] fieldList = classToParse.getDeclaredFields();
        for ( Field actualField : fieldList )
        {
            if ( onlyStatic && !isStatic(actualField) ) { continue; }
            if ( actualField.getName().trim().equals(fieldToFind.trim()) ) { return actualField; }
        }
        return null;
    }

    public static Field[] listClassFields(Class classToParse, boolean onlyStatic) throws Exception
    {
        Field[] fieldList = classToParse.getDeclaredFields();
        ArrayList<Field> classFields = new ArrayList<Field>();

        for ( Field actualField : fieldList )
        {
            if ( onlyStatic && !isStatic(actualField) ) { continue; }
            classFields.add(actualField);
        }

        return classFields.toArray(new Field[classFields.size()]);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Arguments Parser

    public static Object[] parseCommandArguments(String rawCommand) throws Exception
    {
        String rawClassPathToInvoke;
        String[] rawArguments;

        if ( rawCommand.contains(" ") )
        {
            String[] splitCommand = rawCommand.split(" ");

            ArrayList<String> argumentsArrayList = new ArrayList<String>();
            boolean firstPassed = false;
            for ( String actualSplitThing : splitCommand )
            {
                if ( firstPassed ) { argumentsArrayList.add(actualSplitThing); }
                firstPassed = true;
            }

            rawClassPathToInvoke = splitCommand[0];
            rawArguments = argumentsArrayList.toArray(new String[argumentsArrayList.size()]);
        }
        else
        {
            rawClassPathToInvoke = rawCommand;
            rawArguments = null;
        }

        if ( rawClassPathToInvoke.endsWith(");") ) { rawClassPathToInvoke = rawClassPathToInvoke.substring(0, rawClassPathToInvoke.length()-1); }
        else if ( rawClassPathToInvoke.endsWith(".") ) { rawClassPathToInvoke = rawClassPathToInvoke.substring(0, rawClassPathToInvoke.length()-1); }

        return new Object[] { rawClassPathToInvoke, rawArguments };
    }

    // Generate Arguments Array From Raw Argument String Line
    public static Object[] parseInvocationArguments(String rawArguments) throws Exception
    {
        rawArguments = rawArguments.trim();
        if ( !rawArguments.startsWith("(") || !rawArguments.endsWith(")") ) { throw new Exception("Invalid parenthesis !"); }
        if ( rawArguments.equals("()") ) { return new Object[0]; }
        rawArguments = rawArguments.substring(rawArguments.indexOf("(")+1, rawArguments.indexOf(")"));

        ArrayList<Object> argumentsArray = new ArrayList<Object>();

        if ( rawArguments.contains(",") )
        { argumentsArray.add(parseCommandArguments(rawArguments)); }
        else
        {
            String[] splitCommand = rawArguments.split(",");
            for ( String actualArgument : splitCommand ) { argumentsArray.add(parseArgumentLine(actualArgument)); }
        }

        return argumentsArray.toArray();
    }

    // Convert Argument String To Real Argument ( Object )
    private static Object parseArgumentLine(String argumentLine) throws Exception
    {
        argumentLine = argumentLine.trim();

        if ( argumentLine.toLowerCase().equals("null") ) { return null; }
        else if ( !argumentLine.contains(":") ) { throw new Exception("Bad Argument Line !"); }

        String[] splitArgumentLine = argumentLine.split(":");

        String fieldType = splitArgumentLine[0].trim();

        if ( fieldType.equals("String") ) { return splitArgumentLine[1]; }
        else if ( fieldType.equals("int") ) { return Integer.parseInt(splitArgumentLine[1]); }
        else if ( fieldType.equals("double") ) { return Double.parseDouble(splitArgumentLine[1]); }
        else if ( fieldType.equals("float") ) { return Float.parseFloat(splitArgumentLine[1]); }
        else if ( fieldType.equals("File") ) { return new File(splitArgumentLine[1]); }
        else if ( fieldType.equals("boolean") )
        {
            String rawField = splitArgumentLine[1].toLowerCase().trim();

            if ( rawField.equals("true") ) { return true; }
            else if ( rawField.equals("false") ) { return false; }
            else { throw new Exception("Unknown Boolean Value ! [" + splitArgumentLine[1] + "]"); }
        }
        else if ( fieldType.equals("byte") ) { return Byte.parseByte(splitArgumentLine[1]); }
        else { throw new Exception("Unknown Field Type ! [" + fieldType + "]"); }
    }

    // Convert Argument Array To Class Array
    private static Class[] convertArgumentsToClassArray(Object[] arguments) throws Exception
    {
        ArrayList<Class> classArray = new ArrayList<Class>();
        for ( Object actualArgument : arguments ) { classArray.add(actualArgument == null ? null : actualArgument.getClass()); }
        return classArray.toArray(new Class[classArray.size()]);
    }

    // Compare Two Class Arrays
    private static boolean compareClassArray(Class[] firstClassArray, Class[] secondClassArray) throws Exception
    {
        if ( firstClassArray == null || secondClassArray == null ) { throw new Exception("Class Array Are null(s ?) !"); }
        if ( firstClassArray.length != secondClassArray.length ) { System.out.println("Different Size"); return false; }
        for (int i=0; i<firstClassArray.length; i++) { if ( firstClassArray[i] != null && secondClassArray[i] != null && firstClassArray[i] != secondClassArray[i] ) { System.out.println(firstClassArray[i] + " != " + secondClassArray[i]); return false; } }

        return true;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // JRVME - Modifiers Checker

    public static Object invokeStaticMethod(Class methodClass, String methodName, String rawArguments) throws Exception
    {
        Object[] argumentArray = parseInvocationArguments(rawArguments);
        Class[] argumentsType = convertArgumentsToClassArray(argumentArray);

        System.out.println(methodName);
        System.out.println(methodClass);
        System.out.println("\n\n");

        for ( Method actualMethod : listClassMethods(methodClass, true) )
        {
            //System.out.println(argumentArray[0].toString());
            if ( actualMethod.getName().equals(methodName.trim()) && compareClassArray(argumentsType, actualMethod.getParameterTypes()) ) { return _invokeStaticMethod(actualMethod, argumentArray); }
            System.out.println();
        }

        throw new Exception("No Method Found ! [" + methodName + "]");
    }

    public static Object _invokeStaticMethod(Method methodToInvoke, Object[] arguments) throws Exception
    {
        boolean wasPrivate = false;
        if ( Modifier.isPrivate(methodToInvoke.getModifiers()) )
        {
            try { wasPrivate = true; methodToInvoke.setAccessible(true); }
            catch ( Exception e ) { throw new Exception("Error SetAccessible True =>\n" + System_ErrorHandler.convertExceptionToString(e)); }
        }

        Object invokeResult = null;
        try { invokeResult = methodToInvoke.invoke(null, arguments); }
        catch ( Exception e ) { throw new Exception("Error Invoke =>\n" + System_ErrorHandler.convertExceptionToString(e)); }

        if ( wasPrivate )
        {
            try { methodToInvoke.setAccessible(false); }
            catch ( Exception e ) { throw new Exception("Error SetAccessible False =>\n" + System_ErrorHandler.convertExceptionToString(e)); }
        }

        return invokeResult;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Modifiers Checker

    public static boolean isStatic(Field fieldToAnalyze) throws Exception { return  Modifier.isStatic(fieldToAnalyze.getModifiers()); }
    public static boolean isStatic(Method methodToAnalyze) throws Exception { return  Modifier.isStatic(methodToAnalyze.getModifiers()); }

}
