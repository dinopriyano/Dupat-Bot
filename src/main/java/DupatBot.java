import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.sql.Array;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class DupatBot extends TelegramLongPollingBot {
    public void onUpdateReceived(Update update) {

        String[] cmd = update.getMessage().getText().split(" ");
        SendMessage message = new SendMessage();
        String APIParent = "https://corona.lmao.ninja/";

        String userName = "";
        if(update.getMessage().getChat().getLastName() != null)
        {
            userName = update.getMessage().getChat().getFirstName()+" "+update.getMessage().getChat().getLastName();
        }
        else
        {
            userName = update.getMessage().getChat().getFirstName();
        }

        if(cmd[0].equalsIgnoreCase("/start"))
        {
            message.setText("Hello "+userName.trim()+", welcome to Dupat Bot. This is command to communication with me.\n" +
                    "\n" +
                    "General Command\n" +
                    "[1] To start communication with me, use: <pre>/start</pre>\n" +
                    "[2] To see all menu, use: <pre>/help</pre>\n" +
                    "[3] To know info about developer, use: <pre>/devinfo</pre>\n" +
                    "\n" +
                    "Covid-19 Command\n" +
                    "[1] To see list of country where infected Covid-19, use <pre>covid country</pre>\n"+
                    "[2] To see all cases Covid-19, use: <pre>covid all</pre>\n" +
                    "[3] To see Covid-19 cases in a country, use: <pre>covid {country}</pre> Example: <pre>covid indonesia</pre>\n" +
                    "\n" +
                    "I will provide the data as accurately as possible so that you will be comfortable with me :). Thanks for using Dupat Bot. Enjoy :)").enableHtml(true);
        }
        else if(cmd[0].equalsIgnoreCase("/devinfo"))
        {
            message.setText("This bot developed by Dino Priyano\n\nGithub: https://github.com/dinopriyano\nInstagram: https://www.instagram.com/dinopriyano\nFacebook: https://www.facebook.com/dino.priyano.03\nWhatsapp: https://wa.me/6282122420245\nTelegram: https://t.me/dinopriyano\n\nEnjoy use this bot :)").enableHtml(true);
        }
        else if(cmd[0].equalsIgnoreCase("/help"))
        {
            message.setText("This is command to communication with me.\n" +
                    "\n" +
                    "General Command\n" +
                    "[1] To start communication with me, use: <pre>/start</pre>\n" +
                    "[2] To see all menu, use: <pre>/help</pre>\n" +
                    "[3] To know info about developer, use: <pre>/devinfo</pre>\n" +
                    "\n" +
                    "Covid-19 Command\n" +
                    "[1] To see list of country where infected Covid-19, use <pre>covid country</pre>\n"+
                    "[2] To see all cases Covid-19, use: <pre>covid all</pre>\n" +
                    "[3] To see Covid-19 cases in a country, use: <pre>covid {country}</pre> Example: <pre>covid indonesia</pre>\n" +
                    "\n" +
                    "I will provide the data as accurately as possible so that you will be comfortable with me :). Thanks for using Dupat Bot. Enjoy :)").enableHtml(true);
        }
        else if(cmd[0].equalsIgnoreCase("hi") || cmd[0].equalsIgnoreCase("hello") || cmd[0].equalsIgnoreCase("halo"))
        {
            message.setText("Hello "+userName+"!");
        }
        else if(cmd.length == 1 && cmd[0].equalsIgnoreCase("covid"))
        {
            message.setText("Sorry, commend for Covid-19 invalid.\nCovid-19 commend is <pre>covid {country}</pre>, Example: <pre>covid indonesia</pre>").enableHtml(true);
        }
        else if(cmd[0].equalsIgnoreCase("covid"))
        {
            try
            {
                SendMessage wait = new SendMessage();
                wait.setChatId(update.getMessage().getChatId());
                wait.setText("Please wait, i will check the data!");
                execute(wait);

                if(cmd[1].equalsIgnoreCase("all"))
                {
                    try {
                        JSONObject jsonObject = new JSONObject(getData(APIParent+"all"));
                        int kasus = jsonObject.getInt("cases");
                        int mati = jsonObject.getInt("deaths");
                        int recovered = jsonObject.getInt("recovered");
                        long lastUp = jsonObject.getLong("updated");



                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(lastUp);

                        Date date = calendar.getTime();

                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String dateLastUp = format1.format(date);
                        message.setText("Total of all Covid-19\nCases: "+String.valueOf(kasus)+"\nDeaths: "+String.valueOf(mati)+"\nRecovered: "+String.valueOf(recovered)+"\nLast Update: "+dateLastUp);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else if(cmd[1].equalsIgnoreCase("country"))
                {
                    try {
                        JSONArray jsonArray = new JSONArray(getData(APIParent+"countries"));
                        String[] cnt = new String[jsonArray.length()];
                        String listCountry = "";
                        for (int i = 0;i<jsonArray.length();i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            cnt[i] = jsonObject.getString("country");
                        }
                        Arrays.sort(cnt);
                        for(int i = 0;i<cnt.length;i++)
                        {
                            listCountry+=(cnt[i]+"\n");
                        }

                        message.setText(listCountry);
                    }
                    catch (Exception e)
                    {

                    }
                }
                else
                {
                    try {
                        String cty = "";
                        if(cmd.length > 2)
                        {
                            String x = "";
                            for (int i = 1;i<cmd.length;i++)
                            {
                                x+=cmd[i]+" ";
                            }
                            cty = x.trim();
                        }
                        else
                        {
                            cty = cmd[1];
                        }

                        String data = getData(APIParent+"countries/"+cty.toLowerCase());
                        if(data != null)
                        {
                            try
                            {
                                JSONObject jsonObject = new JSONObject(data);
                                String country = jsonObject.getString("country");
                                int kasus = jsonObject.getInt("cases");
                                int mati = jsonObject.getInt("deaths");
                                int recovered = jsonObject.getInt("recovered");
                                int nowKasus = jsonObject.getInt("todayCases");
                                int nowMati = jsonObject.getInt("todayDeaths");
                                int aktif = jsonObject.getInt("active");
                                int kritis = jsonObject.getInt("critical");

                                Date date = new Date();

                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String dateLastUp = format1.format(date);
                                //message.setText("Covid-19 in "+country+"\nCases: "+String.valueOf(kasus)+"\nDeaths: "+String.valueOf(mati)+"\nRecovered: "+String.valueOf(recovered)+"\nLast Update: "+dateLastUp);
                                message.setText("Covid-19 in " +country+
                                        "\nCases: " +kasus+
                                        "\nToday Cases: " +nowKasus+
                                        "\nDeaths: " +mati+
                                        "\nToday Death: " +nowMati+
                                        "\nRecovered: " +recovered+
                                        "\nActive: " +aktif+
                                        "\nCritical: "+kritis);
                            }
                            catch (Exception er)
                            {
                                message.setText("Sorry data for "+cty+" is not available");
                            }
                        }
                        else
                        {
                            message.setText("Sorry this country is not available");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            catch (Exception e)
            {

            }
        }
        else
        {
            message.setText("Sorry, Command not found");
        }

        message.setChatId(update.getMessage().getChatId());

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    String getData(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String getBotUsername() {
        return "dupatbot";
    }

    public String getBotToken() {
        return "1088654621:AAH6ClNiZicGMijpPTvbiWVkO56bo6cnohQ";
    }
}
