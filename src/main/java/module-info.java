module treefx {
  requires javafx.controls;
  requires javafx.media;

  opens com.acme.treefx to javafx.graphics;
}
