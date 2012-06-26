import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class System_Reflection
{

    public static void changeFinalStaticField(String targetName, Class targetClass, Object newValue) throws NoSuchFieldException, IllegalAccessException
    {
        Field targetField = targetClass.getDeclaredField(targetName);
        targetField.setAccessible(true);

        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(targetField, targetField.getModifiers() & ~Modifier.FINAL);

        targetField.set(null, newValue);

        modifiersField.setAccessible(false);
        targetField.setAccessible(false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Nicnl - nicnl25@gmail.com
}
