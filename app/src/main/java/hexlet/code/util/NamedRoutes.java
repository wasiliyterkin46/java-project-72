package hexlet.code.util;

public class NamedRoutes {
    public static String mainPath() {
        return "/";
    }

    public static String urlsPath() {
        return "/urls";
    }

    public static String urlCheckPath(String idString) {
        return urlPath(idString) + "/checks";
    }

    public static String urlCheckPath(Long idLong) {
        String idString = String.valueOf(idLong);
        return urlPath(idString) + "/checks";
    }

    public static String urlPath(String idString) {
        return "/urls/" + idString;
    }

    public static String urlPath(Long idLong) {
        String idString = String.valueOf(idLong);
        return urlPath(idString);
    }
}
