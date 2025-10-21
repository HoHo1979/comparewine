import 'dart:io';
import 'package:excel/excel.dart';
import 'package:file_picker/file_picker.dart';
import '../models/wine_price.dart';
import '../models/winery.dart';

/// Service for reading and writing Excel files containing wine price data
class ExcelService {
  /// Supported suppliers/wineries
  static const List<String> supportedSuppliers = [
    'DUCLOT',
    'SOVEX',
    'LOUIS',
  ];

  /// Pick an Excel file from the device
  Future<File?> pickExcelFile() async {
    final result = await FilePicker.platform.pickFiles(
      type: FileType.custom,
      allowedExtensions: ['xlsx', 'xls'],
      allowMultiple: false,
    );

    if (result != null && result.files.single.path != null) {
      return File(result.files.single.path!);
    }
    return null;
  }

  /// Read Excel file and extract wine price data
  Future<List<WinePrice>> readExcelFile(File file) async {
    final bytes = await file.readAsBytes();
    final excel = Excel.decodeBytes(bytes);

    final winePrices = <WinePrice>[];
    final fileName = file.path.split('/').last.toUpperCase();

    // Detect supplier from filename
    String supplier = 'UNKNOWN';
    for (final sup in supportedSuppliers) {
      if (fileName.contains(sup)) {
        supplier = sup;
        break;
      }
    }

    // Process each sheet
    for (final table in excel.tables.keys) {
      final sheet = excel.tables[table];
      if (sheet == null) continue;

      // Parse based on supplier format
      switch (supplier) {
        case 'DUCLOT':
          winePrices.addAll(_readDuclotFormat(sheet, supplier));
          break;
        case 'SOVEX':
          winePrices.addAll(_readSovexFormat(sheet, supplier));
          break;
        case 'LOUIS':
          winePrices.addAll(_readLouisFormat(sheet, supplier));
          break;
        default:
          winePrices.addAll(_readGenericFormat(sheet, supplier));
      }
    }

    return winePrices;
  }

  /// Read DUCLOT format Excel file
  List<WinePrice> _readDuclotFormat(Sheet sheet, String supplier) {
    final winePrices = <WinePrice>[];

    // Skip header rows (assume first row is header)
    for (int i = 1; i < sheet.maxRows; i++) {
      final row = sheet.row(i);
      if (_isEmptyRow(row)) continue;

      try {
        final winePrice = WinePrice(
          wineName: _getCellValue(row, 0),
          wineVintage: _getCellValue(row, 1),
          price: _parseDouble(_getCellValue(row, 2)),
          priceUnit: _getCellValue(row, 3).isNotEmpty ? _getCellValue(row, 3) : 'EUR',
          bottleSize: _getCellValue(row, 4).isNotEmpty ? _getCellValue(row, 4) : '750ml',
          origin: _getCellValue(row, 5),
          country: _getCellValue(row, 6),
          score: _parseDouble(_getCellValue(row, 7)),
          supplier: supplier,
        );

        if (winePrice.wineName.isNotEmpty) {
          winePrices.add(winePrice);
        }
      } catch (e) {
        print('Error parsing row $i: $e');
      }
    }

    return winePrices;
  }

  /// Read SOVEX format Excel file
  List<WinePrice> _readSovexFormat(Sheet sheet, String supplier) {
    // Similar to DUCLOT but with different column mapping
    return _readDuclotFormat(sheet, supplier);
  }

  /// Read LOUIS format Excel file
  List<WinePrice> _readLouisFormat(Sheet sheet, String supplier) {
    // Similar to DUCLOT but with different column mapping
    return _readDuclotFormat(sheet, supplier);
  }

  /// Read generic format Excel file
  List<WinePrice> _readGenericFormat(Sheet sheet, String supplier) {
    final winePrices = <WinePrice>[];

    // Find header row (look for "name", "price", etc.)
    int headerRow = 0;
    for (int i = 0; i < sheet.maxRows && i < 10; i++) {
      final row = sheet.row(i);
      final firstCell = _getCellValue(row, 0).toLowerCase();
      if (firstCell.contains('name') || firstCell.contains('wine')) {
        headerRow = i;
        break;
      }
    }

    // Read data rows
    for (int i = headerRow + 1; i < sheet.maxRows; i++) {
      final row = sheet.row(i);
      if (_isEmptyRow(row)) continue;

      try {
        final winePrice = WinePrice(
          wineName: _getCellValue(row, 0),
          wineVintage: _getCellValue(row, 1),
          price: _parseDouble(_getCellValue(row, 2)),
          priceUnit: _getCellValue(row, 3).isNotEmpty ? _getCellValue(row, 3) : 'EUR',
          bottleSize: _getCellValue(row, 4).isNotEmpty ? _getCellValue(row, 4) : '750ml',
          origin: _getCellValue(row, 5),
          country: _getCellValue(row, 6),
          score: _parseDouble(_getCellValue(row, 7)),
          supplier: supplier,
        );

        if (winePrice.wineName.isNotEmpty) {
          winePrices.add(winePrice);
        }
      } catch (e) {
        print('Error parsing row $i: $e');
      }
    }

    return winePrices;
  }

  /// Save wine prices to Excel file
  Future<void> saveToExcel(List<WinePrice> winePrices, String filePath) async {
    final excel = Excel.createExcel();
    final sheet = excel['Sheet1'];

    // Write header
    final headers = [
      'Wine Name',
      'Vintage',
      'Price',
      'Price Unit',
      'Bottle Size',
      'Origin',
      'Country',
      'Score',
      'Supplier'
    ];

    for (int i = 0; i < headers.length; i++) {
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: i, rowIndex: 0))
        .value = TextCellValue(headers[i]);
    }

    // Write data
    for (int i = 0; i < winePrices.length; i++) {
      final wine = winePrices[i];
      final row = i + 1;

      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 0, rowIndex: row))
        .value = TextCellValue(wine.wineName);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 1, rowIndex: row))
        .value = TextCellValue(wine.wineVintage);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 2, rowIndex: row))
        .value = DoubleCellValue(wine.price);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 3, rowIndex: row))
        .value = TextCellValue(wine.priceUnit);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 4, rowIndex: row))
        .value = TextCellValue(wine.bottleSize);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 5, rowIndex: row))
        .value = TextCellValue(wine.origin);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 6, rowIndex: row))
        .value = TextCellValue(wine.country);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 7, rowIndex: row))
        .value = DoubleCellValue(wine.score);
      sheet.cell(CellIndex.indexByColumnRow(columnIndex: 8, rowIndex: row))
        .value = TextCellValue(wine.supplier);
    }

    // Save file
    final bytes = excel.encode();
    if (bytes != null) {
      await File(filePath).writeAsBytes(bytes);
    }
  }

  /// Group wine prices by supplier/winery
  List<Winery> groupByWinery(List<WinePrice> winePrices) {
    final wineryMap = <String, Winery>{};

    for (final wine in winePrices) {
      final wineryName = wine.supplier.isNotEmpty ? wine.supplier : 'UNKNOWN';

      if (!wineryMap.containsKey(wineryName)) {
        wineryMap[wineryName] = Winery(name: wineryName);
      }

      wineryMap[wineryName]!.addWinePrice(wine);
    }

    return wineryMap.values.toList();
  }

  /// Helper method to get cell value as string
  String _getCellValue(List<Data?> row, int index) {
    if (index >= row.length || row[index] == null) return '';
    final cell = row[index];
    return cell?.value?.toString() ?? '';
  }

  /// Helper method to parse double from cell
  double _parseDouble(String value) {
    if (value.isEmpty) return 0.0;
    final parsed = double.tryParse(value.replaceAll(',', '.'));
    return parsed ?? 0.0;
  }

  /// Check if row is empty
  bool _isEmptyRow(List<Data?> row) {
    return row.every((cell) => cell == null || cell.value == null || cell.value.toString().isEmpty);
  }
}
