import io.reactivex.rxjava3.core.Observable;
import model.Photo;
import util.PhotoDownloader;
import util.PhotoProcessor;
import util.PhotoSerializer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PhotoCrawler {

    private static final Logger log = Logger.getLogger(PhotoCrawler.class.getName());

    private final PhotoDownloader photoDownloader;

    private final PhotoSerializer photoSerializer;

    private final PhotoProcessor photoProcessor;

    public PhotoCrawler() throws IOException {
        this.photoDownloader = new PhotoDownloader();
        this.photoSerializer = new PhotoSerializer("./photos");
        this.photoProcessor = new PhotoProcessor();
    }

    public void resetLibrary() throws IOException {
        photoSerializer.deleteLibraryContents();
    }

    public void downloadPhotoExamples() {
        photoDownloader.getPhotoExamples()
                .compose(this::processPhotos)
                .subscribe(photoSerializer::savePhoto);

    }

    public void downloadPhotosForQuery(String query) {
        photoDownloader.searchForPhotos(query)
                .compose(this::processPhotos)
                .subscribe(photoSerializer::savePhoto);
    }

    public void downloadPhotosForMultipleQueries(List<String> topics){
        photoDownloader.searchForPhotos(topics)
                .compose(this::processPhotos)
                .subscribe(photoSerializer::savePhoto);
    }

    public Observable<Photo> processPhotos(Observable<Photo> observable){
        return observable
                .filter(photoProcessor::isPhotoValid)
                .map(photoProcessor::convertToMiniature);
    }
}
