import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class GetPropertyValues {
    private InputStream inputStream;
    int numberOfPlayers;
    int round;

    void getPropertyValues() throws IOException {
        try {
            Properties properties = new Properties();
            String propertiesFileName = "config.properties";

            inputStream = getClass().getClassLoader().getResourceAsStream(propertiesFileName);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("property file ' " + propertiesFileName + " ' not found");
            }

            //get the property value
            numberOfPlayers = Integer.parseInt(properties.getProperty("player"));
            round = Integer.parseInt(properties.getProperty("round"));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
