package com.example.weather_bot;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Component
public class WeatherBot extends TelegramLongPollingBot {
    @Value("${telegram.bot.token}")
    private String BOT_TOKEN;

    @Value("${telegram.bot.username}")

    private  String BOT_USERNAME ;
    //private  String BOT_USERNAME ;


    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        String country = update.getMessage().getText();
        if (country.startsWith("/start")) {


            SendMessage sendMessage = new SendMessage(update.getMessage().getChatId().toString(), "enter the name of the city");
            execute(sendMessage);

        }else {

           String weatherData = getWeatherData(country);
           SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), weatherData);
           execute(message);
        }

    }

    @SneakyThrows
    private String getWeatherData(String country) {
        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest request = HttpRequest.newBuilder(URI.create(
                        "http://api.weatherapi.com/v1/current.json" +
                                "?key=ca82b8e4aed4423a92545220230507&" +
                                "q="+country))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            String body = response.body();
            Response responseObject = gson.fromJson(body, Response.class);

            return "Name:  " + responseObject.getLocation().name +
                    "\nRegion:  " + responseObject.getLocation().region +
                    "\nCountry:  " + responseObject.getLocation().country +
                    "\nData and Time:  " + responseObject.getLocation().localtime +
                    "\nTemperature:  " + responseObject.getCurrent().temp_c;


        } else {
            return "No such City or Country";
        }
    }


    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

}
