# Quick Start Guide - Compare Wine Flutter App

This guide will help you get the Compare Wine Flutter app up and running in minutes.

## Prerequisites Check

Before starting, ensure you have Flutter installed:

```bash
flutter --version
```

You should see Flutter 3.0.0 or higher. If not, install Flutter from https://flutter.dev/docs/get-started/install

## Installation Steps

### 1. Navigate to the Project

```bash
cd comparewine/comparewine_flutter
```

### 2. Get Dependencies

```bash
flutter pub get
```

This will download all required packages (excel, file_picker, provider, etc.)

### 3. Check Connected Devices

```bash
flutter devices
```

You should see at least one device (emulator, connected phone, or desktop).

### 4. Run the App

```bash
flutter run
```

Or specify a device:

```bash
flutter run -d chrome          # For web
flutter run -d macos           # For macOS desktop
flutter run -d windows         # For Windows desktop
flutter run -d linux           # For Linux desktop
```

## First Steps in the App

### 1. Load Sample Data

1. Click **Load Excel** in the toolbar
2. Navigate to `assets/wineprice/` folder
3. Select `Test.xlsx` or `Winery.xlsx`
4. Click Open

### 2. Explore the Data

- Scroll through the wine price table
- Click column headers to sort
- View different suppliers (shown with color-coded badges)

### 3. Edit a Wine Entry

1. Click the **pencil icon** on any row
2. Modify the wine information
3. Click **Save**

### 4. Compare Suppliers

1. Click the **Compare** button
2. View statistics for each supplier:
   - Number of wines
   - Average price

### 5. Save Your Changes

1. Click the **Save** button
2. Your changes are written back to the Excel file

## Creating Your Own Wine Price List

### Excel Format

Create an Excel file with these columns:

| Column | Description | Example |
|--------|-------------|---------|
| A: Wine Name | Name of the wine | Château Margaux |
| B: Vintage | Year | 2015 |
| C: Price | Price (numeric) | 450.00 |
| D: Price Unit | Currency | EUR |
| E: Bottle Size | Size with unit | 750ml |
| F: Origin | Wine region | Bordeaux |
| G: Country | Country of origin | France |
| H: Score | Wine score (0-100) | 95.0 |

### Example Excel Content

```
Wine Name            | Vintage | Price  | Price Unit | Bottle Size | Origin     | Country | Score
---------------------|---------|--------|------------|-------------|------------|---------|------
Château Margaux      | 2015    | 450.00 | EUR        | 750ml       | Bordeaux   | France  | 95.0
Château Latour       | 2016    | 520.00 | EUR        | 750ml       | Pauillac   | France  | 97.0
Domaine de la Romanée| 2014    | 1200.00| EUR        | 750ml       | Burgundy   | France  | 98.0
```

### Supplier Naming Convention

To enable automatic supplier detection, include the supplier name in the filename:

- `DUCLOT_prices_2024.xlsx` → Detected as DUCLOT
- `SOVEX_january.xlsx` → Detected as SOVEX
- `LOUIS_wines.xlsx` → Detected as LOUIS

## Hot Reload

While developing, you can hot reload changes:

1. Make changes to the code
2. Press `r` in the terminal (hot reload)
3. Press `R` for hot restart
4. Press `q` to quit

## Building for Production

### Mobile Apps

**Android APK:**
```bash
flutter build apk --release
```
Output: `build/app/outputs/flutter-apk/app-release.apk`

**iOS:**
```bash
flutter build ios --release
```
Then open `ios/Runner.xcworkspace` in Xcode to archive

### Desktop Apps

**Windows:**
```bash
flutter build windows --release
```
Output: `build/windows/runner/Release/`

**macOS:**
```bash
flutter build macos --release
```
Output: `build/macos/Build/Products/Release/`

**Linux:**
```bash
flutter build linux --release
```
Output: `build/linux/x64/release/bundle/`

### Web App

```bash
flutter build web --release
```
Output: `build/web/`

Deploy the `build/web/` folder to any static hosting service.

## Common Issues

### "Package not found" error

```bash
flutter pub get
```

### "No devices found"

- For mobile: Start an emulator or connect a device
- For desktop: Enable desktop support:
  ```bash
  flutter config --enable-windows-desktop
  flutter config --enable-macos-desktop
  flutter config --enable-linux-desktop
  ```

### App crashes on startup

```bash
flutter clean
flutter pub get
flutter run
```

### Excel files won't load

- Ensure files are in `.xlsx` or `.xls` format
- Check file permissions
- Try with the sample files first

## Development Tips

### Project Structure

- `lib/models/` - Add new data models here
- `lib/services/` - Add business logic here
- `lib/screens/` - Add new screens here
- `lib/widgets/` - Add reusable widgets here
- `lib/providers/` - Add state management here

### Adding Dependencies

1. Edit `pubspec.yaml`
2. Add package under `dependencies:`
3. Run `flutter pub get`

### Debugging

Enable debug mode:
```bash
flutter run --debug
```

Use DevTools:
```bash
flutter run
# Then press 'w' to open DevTools
```

## Next Steps

- Explore the code in `lib/`
- Customize the UI theme in `lib/main.dart`
- Add new supplier formats in `lib/services/excel_service.dart`
- Read the full README.md for detailed documentation

## Support

- Flutter docs: https://flutter.dev/docs
- Provider docs: https://pub.dev/packages/provider
- Excel package: https://pub.dev/packages/excel

---

Happy coding! 🚀
