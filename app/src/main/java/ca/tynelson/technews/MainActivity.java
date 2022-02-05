package ca.tynelson.technews;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.companion.WifiDeviceFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    private RecyclerView newsRV,categoryRV;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryRVModel> categoryRVModelArrayList;
    private CategoryRVAdapter categoryRVAdapter;
    private NewsRVAdapter newsRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryRVModelArrayList = new ArrayList<>();
        newsRVAdapter = new NewsRVAdapter(articlesArrayList,this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModelArrayList,this,this::onCategoryClick);
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();
    }

    private void getCategories() {
        categoryRVModelArrayList.add(new CategoryRVModel("All", "https://i.cbc.ca/1.6259351.1637678933!/cumulusImage/httpImage/image.jpg_gen/derivatives/16x9_620/a-truck-plows-through-a-puddle-on-kings-road-sydney-river-ns-on-tuesday-nov-23-2021-cape-breton-island-forecast-to-get-up-to-100mm-of-rain-and-high-winds.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Technology", "https://www.comingsoon.net/assets/uploads/2021/11/EG_Harmonix_1920x1080.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Science", "https://static01.nyt.com/images/2021/11/23/science/23dart-launch1/23dart-launch1-facebookJumbo.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Sports", "https://www.sportsnet.ca/wp-content/uploads/2021/10/jaysfinale-1040x572.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("General", "https://images.thestar.com/-jacu45dC-HlUXRDPWV2Di2_O7c=/1200x800/smart/filters:cb(1637721965279)/https://www.thestar.com/content/dam/thestar/news/canada/2021/11/23/covid-19-coronavirus-updates-toronto-canada-november-23/golf.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Business", "https://smartcdn.prod.postmedia.digital/vancouversun/wp-content/uploads/2021/11/png1120-port-backlog-19.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Entertainment", "https://imagesvc.meredithcorp.io/v3/mm/image?q=85&c=sc&poi=%5B492%2C915%5D&w=1200&h=600&url=https%3A%2F%2Fstatic.onecms.io%2Fwp-content%2Fuploads%2Fsites%2F20%2F2021%2F11%2F23%2Ftravis-barker-4.jpg"));
        categoryRVModelArrayList.add(new CategoryRVModel("Health", "https://d2jx2rerrg6sh3.cloudfront.net/image-handler/picture/2016/3/Artificially_Colored_MRI_Scan_Of_Human_Brain-Daisy_Daisy_a8c5d8bbbf824bc8932308e30187510f-620x480.jpg"));
        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category) {
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=ca&category="+ category +"&apiKey=9b1a951b290d4a49bed9ab7db711ccce";
        String url = "https://newsapi.org/v2/top-headlines?country=ca&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=9b1a951b290d4a49bed9ab7db711ccce";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class);
        Call<NewsModel> call;

        if(category.equals("All")) {
            call = retrofitAPI.getALLNews(url);
        } else {
            call = retrofitAPI.getNewsByCategory(categoryURL);
        }

        call.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel newsModel = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModel.getArticles();
                for(int i=0; i<articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(), articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()));
                }
                newsRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to get news", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModelArrayList.get(position).getCategory();
        getNews(category);
    }
}














