# Compare Wine - Flutter Edition

A Flutter application for comparing wine prices from multiple suppliers through Excel file imports. This app provides an interactive spreadsheet interface for viewing, editing, and comparing wine price data.

## Features

- **Excel File Import**: Load wine price data from .xlsx and .xls files
- **Interactive Spreadsheet**: View and edit wine prices in a table format
- **Price Comparison**: Compare prices across multiple suppliers (DUCLOT, Sovex-Woltner, Louis Vialard)
- **Data Management**: Sort, filter, and edit wine price entries
- **Export to Excel**: Save modified data back to Excel files
- **Supplier Analytics**: View statistics and comparisons by supplier/winery

## Technology Stack

- **Flutter**: Cross-platform mobile/desktop framework
- **Provider**: State management
- **excel**: Excel file reading and writing
- **file_picker**: File selection
- **data_table_2**: Enhanced data table widget

## Project Structure

```
comparewine_flutter/
├── lib/
│   ├── main.dart                           # App entry point
│   ├── models/                             # Data models
│   │   ├── wine_price.dart                 # WinePrice model
│   │   └── winery.dart                     # Winery model
│   ├── services/                           # Business logic
│   │   └── excel_service.dart              # Excel read/write service
│   ├── providers/                          # State management
│   │   └── wine_data_provider.dart         # Wine data provider
│   ├── screens/                            # UI screens
│   │   └── spreadsheet_view.dart           # Main spreadsheet view
│   └── widgets/                            # Reusable widgets
│       └── wine_data_table.dart            # Data table widget
├── assets/
│   └── wineprice/                          # Sample Excel files
├── pubspec.yaml                            # Dependencies
└── README.md                               # This file
```

## Getting Started

### Prerequisites

- Flutter SDK 3.0.0 or higher
- Dart SDK 3.0.0 or higher

### Installation

1. Clone the repository:
   ```bash
   cd comparewine/comparewine_flutter
   ```

2. Install dependencies:
   ```bash
   flutter pub get
   ```

3. Run the app:
   ```bash
   flutter run
   ```

### Building for Production

**Android:**
```bash
flutter build apk
```

**iOS:**
```bash
flutter build ios
```

**Desktop (Windows/macOS/Linux):**
```bash
flutter build windows
flutter build macos
flutter build linux
```

## Usage

### Loading Excel Files

1. Click the **Load Excel** button in the toolbar
2. Select an Excel file (.xlsx or .xls) containing wine price data
3. The app will automatically detect the supplier format and parse the data

### Viewing Data

- The spreadsheet displays all wine prices with the following columns:
  - Wine Name
  - Vintage
  - Price
  - Price Unit
  - Bottle Size
  - Origin
  - Country
  - Score
  - Supplier

### Editing Data

1. Click the **Edit** icon (pencil) on any row
2. Modify the wine price information in the dialog
3. Click **Save** to apply changes

### Deleting Entries

1. Click the **Delete** icon (trash) on any row
2. Confirm the deletion in the dialog

### Sorting

- Click on any column header to sort by that column
- Click again to reverse the sort order

### Comparing Suppliers

1. Load Excel files from multiple suppliers
2. Click the **Compare** button in the toolbar
3. View comparison statistics including:
   - Number of wines per supplier
   - Average price per supplier

### Saving Changes

1. Make your edits to the wine price data
2. Click the **Save** button in the toolbar
3. The data will be saved back to the original Excel file

## Excel File Format

The app supports multiple supplier formats. For generic formats, use this structure:

| Wine Name | Vintage | Price | Price Unit | Bottle Size | Origin | Country | Score |
|-----------|---------|-------|------------|-------------|--------|---------|-------|
| Château Margaux | 2015 | 450.00 | EUR | 750ml | Bordeaux | France | 95.0 |
| Château Latour | 2016 | 520.00 | EUR | 750ml | Pauillac | France | 97.0 |

### Supported Suppliers

The app has built-in parsers for:
- **DUCLOT**: Wine distributor
- **SOVEX**: Sovex-Woltner wine supplier
- **LOUIS**: Louis Vialard wine supplier

Supplier detection is automatic based on the filename.

## Data Models

### WinePrice

Represents individual wine pricing information:

```dart
class WinePrice {
  String wineName;
  String wineVintage;
  double price;
  String priceUnit;
  String bottleSize;
  String origin;
  String country;
  double score;
  String supplier;
}
```

### Winery

Aggregates multiple wine prices by winery:

```dart
class Winery {
  String name;
  List<WinePrice> winePrices;
}
```

## Customization

### Adding New Supplier Formats

To add support for a new supplier format, edit `lib/services/excel_service.dart`:

1. Add the supplier name to `supportedSuppliers`
2. Create a new parsing method (e.g., `_readNewSupplierFormat`)
3. Add a case in the `readExcelFile` switch statement

### Customizing UI Colors

Edit the theme in `lib/main.dart`:

```dart
theme: ThemeData(
  colorScheme: ColorScheme.fromSeed(
    seedColor: Colors.yourColor,
  ),
),
```

### Customizing Supplier Colors

Edit the `_getSupplierColor` method in `lib/widgets/wine_data_table.dart`.

## Comparison with Java/Vaadin Version

This Flutter app replicates the functionality of the original Java/Vaadin web application:

### Similarities
- Excel file reading and writing
- Spreadsheet-style interface
- Supplier-specific parsing logic
- Save functionality
- Price comparison features

### Differences
- **Cross-platform**: Runs on mobile, desktop, and web
- **Modern UI**: Material Design 3
- **Offline-first**: No server required
- **Touch-optimized**: Works on mobile devices
- **Lightweight**: Smaller footprint than Java/Vaadin

## Troubleshooting

### Excel files won't load

- Ensure the file is in .xlsx or .xls format
- Check that the file is not corrupted
- Verify the file permissions

### Data not saving

- Ensure you loaded a file before trying to save
- Check write permissions for the file location

### App crashes on startup

- Run `flutter clean` and `flutter pub get`
- Ensure you're using Flutter 3.0.0 or higher

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is part of the Compare Wine suite, developed by Seven Pyramid.

## Support

For issues and questions, please create an issue in the repository.

## Future Enhancements

- [ ] Cloud storage integration
- [ ] Real-time collaboration
- [ ] Advanced filtering and search
- [ ] Price trend analysis
- [ ] PDF export
- [ ] Barcode scanning for wine lookup
- [ ] Wine recommendation engine
- [ ] Multi-language support

## Version History

### 0.0.1 (Current)
- Initial release
- Excel import/export
- Basic spreadsheet view
- Supplier comparison
- Edit and delete functionality

---

Built with Flutter by Seven Pyramid
