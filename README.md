# Painting App - lp-21034

A simple Android painting app that allows users to draw freehand, select brush colors, adjust brush size, and save their drawings to the device gallery. The app also supports undo and redo functionality for enhancing the user experience.

## Features

- Freehand drawing on a canvas.
- Brush size adjustment using a slider.
- Color selection (Red, Green, Blue).
- Undo and redo functionality for easy corrections.
- Clear the canvas with a single click.
- Save drawings to the device gallery.
- Intuitive and simple user interface.

## Screenshots

![image](https://github.com/user-attachments/assets/a6a26571-90cd-4b9e-a7f0-10d1dd3f51d5)


## Requirements

- Android Studio 4.0 or higher
- Gradle 6.5 or higher
- Android SDK 29 or higher
- Java 8 or higher

## Installation

1. Clone this repository to your local machine using:

    ```bash
    git clone https://github.com/yourusername/paintingapp.git
    ```

2. Open the project in Android Studio.

3. Build and run the project on an emulator or a physical device.

4. Enjoy painting!

## How to Use

1. **Draw on the Canvas**: Use your finger or stylus to draw on the canvas.

2. **Adjust Brush Size**: Use the brush size slider to increase or decrease the brush size.

3. **Select Colors**: Tap the color buttons (Red, Green, Blue) to change the brush color.

4. **Undo/Redo**: Use the undo and redo buttons to revert or restore your last drawing action.

5. **Clear Canvas**: Tap the clear button to erase the entire canvas.

6. **Save Drawing**: Tap the save button to save your artwork to the device gallery.

## Project Structure

```plaintext
app/
├── src/
│   ├── main/
│   │   ├── java/com/example/paintingapp/
│   │   │   ├── MainActivity.java  # Main activity where user interaction is handled
│   │   │   ├── DrawingView.java   # Custom view for drawing canvas
│   │   └── res/
│   │       ├── layout/activity_main.xml  # Main UI layout for the app
│   │       ├── values/                # Holds themes, strings, and colors
│   ├── test/                          # Unit tests
│   ├── androidTest/                   # Instrumented tests
├── build.gradle                       # Gradle configuration
├── AndroidManifest.xml                # Manifest file
