# Tidal Android Downloader

Android application for searching and downloading music from Tidal with high-quality audio support.

## Features

- 🔍 **Search** - Search for artists, albums, and tracks
- ⬇️ **Download** - Download tracks in high-quality audio formats
- 📋 **Queue Management** - Add multiple tracks to download queue
- 📊 **Download Progress** - Monitor download progress in real-time
- 🎵 **Music Organization** - Automatically organize downloaded music by artist and album

## Architecture

The app follows MVVM (Model-View-ViewModel) architecture with:

- **Models**: Data classes for Artist, Album, Track
- **Service**: TidalService interface with MockTidalService implementation
- **Repository**: TidalRepository for data management
- **ViewModels**: SearchViewModel and DownloadsViewModel for UI logic
- **Fragments**: SearchFragment and DownloadsFragment for UI
- **Download Manager**: TidalDownloadManager for handling downloads

## Project Structure

```
app/src/main/java/com/tidal/android/
├── MainActivity.kt
├── TidalApplication.kt
├── model/
│   ├── Artist.kt
│   ├── Album.kt
│   └── Track.kt
├── service/
│   ├── TidalService.kt
│   └── MockTidalService.kt
├── repository/
│   └── TidalRepository.kt
├── download/
│   ├── DownloadTask.kt
│   └── TidalDownloadManager.kt
├── ui/
│   ├── search/
│   │   ├── SearchFragment.kt
│   │   ├── SearchViewModel.kt
│   │   └── SearchViewModelFactory.kt
│   └── downloads/
│       ├── DownloadsFragment.kt
│       ├── DownloadsViewModel.kt
│       ├── DownloadsViewModelFactory.kt
│       └── DownloadsAdapter.kt
└── util/
    ├── Constants.kt
    └── Result.kt
```

## Dependencies

- AndroidX AppCompat
- AndroidX Fragment
- AndroidX Lifecycle
- Material Design
- Gson for JSON serialization
- Kotlin Coroutines

## Usage

### Search for Music

1. Open the Search tab
2. Select search type (Artists, Albums, or Tracks)
3. Enter search query
4. Select items and add to queue

### Download Music

1. Go to Downloads tab
2. Click "Start Downloads" to begin downloading queued tracks
3. Monitor download progress
4. Long press to cancel individual downloads

## Getting Started

### Prerequisites

- Android Studio 4.2 or higher
- Android API level 21 or higher
- Kotlin 1.5 or higher

### Installation

1. Clone the repository
2. Open in Android Studio
3. Build and run the app

## Configuration

Edit `Constants.kt` to configure:

- API base URL
- Download folder name
- Maximum concurrent downloads
- Preferences storage

## API Integration

Currently using MockTidalService. To integrate with real Tidal API:

1. Implement `TidalService` interface
2. Add network request handling (Retrofit recommended)
3. Implement authentication
4. Update error handling

## License

MIT License - see LICENSE file for details

## Contributing

Contributions are welcome! Please submit pull requests or open issues for bug reports.
