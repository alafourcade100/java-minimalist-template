package py.com.code100.infrastructure.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

public class TextUtils {

    /**
     * Obtener un texto aleatorio
     *
     * @param charactersForGenerate Carácteres para generar
     * @param size                  Tamaño de la contraseña
     * @return Retorna la contraseña generada
     */
    public static String randomText(String charactersForGenerate, int size) {
        String pass = "";
        Random ran = new Random();
        for (int i = 0; i < size; i++) {
            pass += charactersForGenerate.charAt(ran.nextInt(charactersForGenerate.length()));
        }
        return pass;
    }

    public static String[] split(String text, String separator, boolean stripSpaces) {
        if (stripSpaces)
            return text.trim().split("\\s*" + separator + "\\s*");
        else
            return text.split(separator);
    }

    public static String[] split(String text, String separator) {
        return split(text, separator, true);
    }

    public static String[] split(String text) {
        return text.trim().split("\\s+");
    }

    public static String join(Iterable lst, String sep) {
        String s = "";
        if (lst != null) {
            Iterator iterator = lst.iterator();
            Object e;
            boolean first = true;
            while (iterator.hasNext()) {
                e = iterator.next();
                if (e != null) {
                    if (!first) s += sep;
                    s += e.toString();
                    first = false;
                }
            }
        }
        return s;
    }

    public static String join(Object[] lst, String sep) {
        String s = "";
        boolean first = true;
        for (Object e : lst) {
            if (!first) s += sep;
            s += e.toString();
            first = false;
        }
        return s;
    }

    public static String join(Object[] lst, int from, String sep) {
        return join(lst, from, lst.length - 1, sep);
    }

    public static String join(Object[] lst, int from, int to, String sep) {
        String s = "";
        for (int i = from; i <= to; i++) {
            Object e = lst[i];
            if (i != from) s += sep;
            s += e.toString();
        }
        return s;
    }

    public static Character capital(String s) {
        if (s == null || (s.length() == 0))
            return null;
        Character firstChar = s.charAt(0);
        return Character.toUpperCase(firstChar);
    }

    public static String capitalize(String s, boolean sensitive) {
        return capitalize(sensitive ? s.toLowerCase() : s);
    }

    public static String capitalize(String s) {
        if (s == null || (s.length() == 0))
            return "";
        String firstChar = String.valueOf(s.charAt(0)).toUpperCase();
        return (s.length() > 1) ? firstChar + s.substring(1) : firstChar;
    }

    public static String applyDotThousandsSeparator(Number value) {
        if (value == null)
            return "0";
        NumberFormat nf = DecimalFormat.getInstance(new Locale("pt", "BR"));
        DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
        customSymbol.setGroupingSeparator('.');
        ((DecimalFormat)nf).setDecimalFormatSymbols(customSymbol);
        nf.setGroupingUsed(true);
        return nf.format(value.longValue());
    }

    public static String getMesStrPorNumero(Integer mes) {
        return Arrays.asList(
                "Enero",
                "Febrero",
                "Marzo",
                "Abril",
                "Mayo",
                "Junio",
                "Julio",
                "Agosto",
                "Septiembre",
                "Octubre",
                "Noviembre",
                "Diciembre"
        ).get(mes - 1);
    }
}
