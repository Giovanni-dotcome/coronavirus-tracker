package io.giovannidevito.coronavirustracker.services;

import io.giovannidevito.coronavirustracker.modules.LocationStats;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CoronaVirusDataService {
    CSVFormat csvFormat = CSVFormat.DEFAULT;
    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationStats> allStats = new ArrayList<>();
    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationStats> newStats = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        StringReader csvBodyReader = new StringReader(httpResponse.body());
        Iterable<CSVRecord> records = CSVFormat.RFC4180.parse(csvBodyReader);
//        for (CSVRecord record : records) {
//            String columnOne = record.get(0);
//            String columnTwo = record.get(1);
//            String columnThree = record.get(record.size()-1);
//            System.out.println(columnOne);
//            System.out.println(columnTwo);
//            System.out.println(columnThree);
//        }
        for (CSVRecord record : records) {
//            System.out.println(record);
            LocationStats locationStat = new LocationStats();
            locationStat.setState(String.valueOf(record.get(0)));
            locationStat.setCountry(String.valueOf(record.get(1)));
            locationStat.setLatestTotalCases(Integer.parseInt(record.get(record.size()-1)));
            System.out.println(locationStat);
            newStats.add(locationStat);
//            int latestCases = Integer.parseInt(record.get(record.size() - 1));
//            int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
//            locationStat.setLatestTotalCases(latestCases);
//            locationStat.setDiffFromPrevDay(latestCases - prevDayCases);
//            newStats.add(locationStat);
        }
//        this.allStats = newStats;
    }
}
