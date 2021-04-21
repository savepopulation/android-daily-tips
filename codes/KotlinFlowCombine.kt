	val state = MutableStateFlow(HomeViewState())
    private val _state = MutableStateFlow(HomeViewState())

    private val genreList = MutableStateFlow(listOf<GenreData>())
    private val chartArtists = MutableStateFlow(listOf<ArtistData>())
    private val chartPodcasts = MutableStateFlow(listOf<PodcastData>())
    private val chartAlbums = MutableStateFlow(listOf<AlbumData>())

    @OptIn(ExperimentalCoroutinesApi::class)
    fun loadData() {
           /**
            * Returns a Flow whose values are generated with transform function
            * by combining the most recently emitted values by each flow.
            * Reach to more information
			* https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-		core/kotlinx.coroutines.flow/combine.html
            */
           combine(
                    genreList.flatMapLatest { genreRepository.fetchGenreList() },
                    chartPodcasts.flatMapLatest { mainRepository.fetchChartPodcasts() },
                    chartArtists.flatMapLatest { mainRepository.fetchChartArtists() },
                    chartAlbums.flatMapLatest { mainRepository.fetchChartAlbums() }
            ) { genres, podcasts, artists, albums ->
                state.value.copy(
                    genres = genres,
                    podcasts = podcasts,
                    artists = artists,
                    albums = albums
                )
            }.launchIn(viewModelScope)
    }