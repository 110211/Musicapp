//package com.huce.hma.presentation.app.playmusic;
//
//public class MusicActivity extends AppCompatActivity {
//    private SharedPreferences prefs;
//    private RecyclerView recyclerView;
//    private SongAdapter adapter;
//    private List<Song> favoriteSongs = new   ArrayList<>();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_music);
//        prefs = getSharedPreferences("UserPreferences", MODE_PRIVATE);
//
//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        adapter = new SongAdapter(this, favoriteSongs);
//        recyclerView.setAdapter(adapter);
//
//        fetchFavoriteSongs();
//    }
//
//    private void fetchFavoriteSongs() {
//        Set<String> favoriteIds = prefs.getStringSet("favoriteSongs", new HashSet<>());
//        for (String id : favoriteIds) {
//            fetchSongDetails(Long.parseLong(id));
//        }
//    }
//
//    private void fetchSongDetails(long songId) {
//        String url = "/api/v1/song+ selectedSong.getId()/" + songId;
//        // Assume we use Retrofit or similar library to make the API call
//        Song song = new Song(songId, "Song Name", "Artist Name", "Song Path", "Thumbnail URL");
//        favoriteSongs.add(song);
//        adapter.notifyDataSetChanged();  // Make sure this is done on the UI thread
//    }
//}
//
