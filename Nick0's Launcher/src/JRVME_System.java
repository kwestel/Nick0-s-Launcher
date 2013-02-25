import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;

public class JRVME_System
{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Hook Center

    public static void commandReceived(String rawCommandText)
    {
        /*try
        {
            if ( rawCommandText.trim().equals("help") ) { displayHelpMessage(); }
            else if ( rawCommandText.trim().equals("initializeClassLoader") ) { initializeCommand(); }
            else if ( rawCommandText.trim().equals("listLoadedClasses") ) { listLoadedClasses(); }
            else { intelligentReflexionManager(rawCommandText); }
        }
        catch ( Exception e ) { displayFatalErrorMessage(e); }*/
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Message Center

    public static final String messageStart = "Nick0's Launcher - Java Reflection VM Changer\n__________________________________\n\n";

    private static final String welcomeMessage =
        "Bienvenue dans la J.R.V.M.E.\n\n" +
        //"Afin d'utiliser la JRVME, vous devez l'initialiser avec le ClassLoader de Minecraft.\n" +
        //"Lancez Minecraft, et tapez simplement \"initializeClassLoader\" afin d'initialiser le système.\n\n" +
        //"Utilisez ensuite la commande \"help\" afin de vous aider dans votre vénérable quête.";
        "Pour l'instant, les fonctionalitées de la JRVME sont désactivées.";
    public static void displayWelcomeMessage() { GuiForm_Console.updateJRVMEContent(welcomeMessage); }

    private static final String helpMessage =
        "Aide - Liste des commandes :\n" +
        "   - help : Affiche ce message.\n" +
        "   - initializeClassLoader : Permet de récupérer le ClassLoader ayant chargé Minecraft et d'initialiser la JRVME.";
    private static void displayHelpMessage() { GuiForm_Console.updateJRVMEContent(helpMessage); }

    private static final String fatalErrorMessage =
        "Erreur fatale !\n" +
        "Une exception innatendue est survenue lors de l'éxécution de la commande !";
    private static void displayFatalErrorMessage(Exception e) { GuiForm_Console.updateJRVMEContent(fatalErrorMessage + "\n\nException : " + System_ErrorHandler.convertExceptionToString(e)); }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // JRVME - Initialize Method

    private static final String initializeError =
        "initializeClassLoader - Erreur !\n" +
        "Apparemment, le ClassLoader n'est pas valide. Est-ce que Minecraft est lancé ?";

    private static void initializeCommand() throws Exception
    {
        minecraftClassLoader = System_MinecraftLoader.MC_ClassLoader;

        if ( minecraftClassLoader == null ) { GuiForm_Console.updateJRVMEContent(initializeError); }
        else
        {
            String winMessage = "initializeClassLoader - Success !\nLe ClassLoader est maintenant initialisé !\n\n" +
                "ClassLoader : " + minecraftClassLoader + "\n" +
                "HashCode : " + minecraftClassLoader.hashCode() + "\n" +
                "Class : " + minecraftClassLoader.getClass() + "\n" +
                "Parent : " + minecraftClassLoader.getParent().getClass();

            GuiForm_Console.updateJRVMEContent(winMessage);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // List Loaded Classes

    private static void listLoadedClasses() throws Exception
    {
        if ( minecraftClassLoader == null ) { GuiForm_Console.updateJRVMEContent("listLoadedClasses - Erreur !\nPas de ClassLoader chargé !"); }

        Class[] loadedClasses = JRVME_ClassScope.getLoadedClasses(minecraftClassLoader);

        ArrayList<String> loadedClassNames = new ArrayList<String>();
        for ( Class actualClass : loadedClasses ) { loadedClassNames.add(actualClass.toString()); }

        Collections.sort(loadedClassNames);

        String loadedClassesList = "Liste des " + loadedClasses.length + " classes chargées :\n\n";
        for ( String actualClassName : loadedClassNames ) { loadedClassesList += "    - " + actualClassName + "\n"; }

        GuiForm_Console.updateJRVMEContent(loadedClassesList);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Intelligent Reflexion Manager

    // Main IRM Function
    private static void intelligentReflexionManager(String rawCommand) throws Exception
    {
        String[] splitedCommands = rawCommand.split(" ");

        String packageAddress = splitedCommands[0];

        if ( packageAddress.toLowerCase().trim().endsWith(")") || packageAddress.toLowerCase().trim().endsWith(");") )
        {
            if ( packageAddress.contains("=") ) { throw new Exception("Cannot define a method result !"); }
            _IRM_MethodCaller(rawCommand);
        }
        else if ( packageAddress.toLowerCase().trim().endsWith(".") )
        {
            if ( packageAddress.contains("=") ) { throw new Exception("Cannot define a list !"); }
            _IRM_ListClass(rawCommand);
        }
        else
        {

        }
    }

    // User Call Method
    private static void _IRM_MethodCaller(String rawCommand) throws Exception
    {
        Object[] parsedArguments = JRVME_Helper.parseCommandArguments(rawCommand);
        String rawClassPathToInvoke = (String)parsedArguments[0];
        final String finalRawName = rawClassPathToInvoke;
        String[] rawArguments = (String[])parsedArguments[1];

        System.out.println("XDXDXD : " + rawCommand);

        Class[] loadedClasses = JRVME_ClassScope.getLoadedClasses(minecraftClassLoader);

        Class firstClass = null;
        for ( Class actualLoadedClass : loadedClasses )
        {
            if ( rawClassPathToInvoke.startsWith(actualLoadedClass.getName()) )
            {
                firstClass = actualLoadedClass;
                break;
            }
        }
        if ( firstClass == null ) { throw new Exception("Class Not Found In Class Loader !"); }

        rawClassPathToInvoke = rawClassPathToInvoke.substring(firstClass.getName().length()+1, rawClassPathToInvoke.length());

        System.out.println("ILOL : " + rawClassPathToInvoke);

        if ( rawClassPathToInvoke.contains(".") )
        {
        }
        else
        {
            String methodName = rawClassPathToInvoke.substring(0, rawClassPathToInvoke.indexOf("("));
            String argumentsLine = rawClassPathToInvoke.substring(rawClassPathToInvoke.indexOf("("), rawClassPathToInvoke.length());

            System.out.println("Invoke Method : " + methodName + " / " + argumentsLine);

            try
            {
                Object invokeResult = JRVME_Helper.invokeStaticMethod(firstClass, methodName, argumentsLine);

                String interpretedResult;
                try
                {
                    if ( invokeResult == null ) { interpretedResult = "Nothing ! ( null )"; }
                    else { interpretedResult = "Result : " + invokeResult.toString() + " [" + invokeResult.getClass() + "]"; }
                }
                catch ( Throwable t ) { interpretedResult = ""; }

                GuiForm_Console.updateJRVMEContent("Method Called !\n\n    - Method : " + finalRawName + "\n\n" + interpretedResult);
            }
            catch ( Exception e ) { GuiForm_Console.updateJRVMEContent("Fatal Error !\n\n" + System_ErrorHandler.convertExceptionToString(e)); }
        }
    }

    // User Ask Class Listing
    private static void _IRM_ListClass(String rawCommand) throws Exception
    {
        Object[] parsedArguments = JRVME_Helper.parseCommandArguments(rawCommand);
        String rawClassPathToInvoke = (String)parsedArguments[0];
        // String[] rawArguments = (String[])parsedArguments[1];

        Class[] loadedClasses = JRVME_ClassScope.getLoadedClasses(minecraftClassLoader);

        Class firstClass = null;
        for ( Class actualLoadedClass : loadedClasses )
        {
            if ( rawClassPathToInvoke.startsWith(actualLoadedClass.getName()) )
            {
                firstClass = actualLoadedClass;
                break;
            }
        }
        if ( firstClass == null ) { throw new Exception("Class Not Found In Class Loader !"); }

        if ( !rawClassPathToInvoke.trim().equals(firstClass.getName().trim()) )
        {
            rawClassPathToInvoke = rawClassPathToInvoke.substring(firstClass.getName().length(), rawClassPathToInvoke.length());
        }
        else
        {
            String message = "Static Class Methods :";
            for ( Method actualMethod : JRVME_Helper.listClassMethods(firstClass, true) ) { message += "\n    - " + actualMethod.toString(); }
            message += "\n___________________________\n\nStatic Class Fields :";
            for ( Field actualField : JRVME_Helper.listClassFields(firstClass, true) ) { message += "\n    - " + actualField.toString(); }

            GuiForm_Console.updateJRVMEContent(message);
        }
    }

    // User Call Something Else ( Read/Write => Private/Public => Instance/Static => Field/Class/Interface )
    private static void _IRM_ClassManager(String rawCommand) throws Exception
    {

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // JRVME Fields

    public static ClassLoader minecraftClassLoader = null;

}
