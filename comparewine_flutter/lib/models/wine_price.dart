/// Represents individual wine pricing information
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

  WinePrice({
    required this.wineName,
    this.wineVintage = '',
    this.price = 0.0,
    this.priceUnit = 'EUR',
    this.bottleSize = '750ml',
    this.origin = '',
    this.country = '',
    this.score = 0.0,
    this.supplier = '',
  });

  /// Create WinePrice from Excel row data
  factory WinePrice.fromExcelRow(List<dynamic> row, String supplier) {
    return WinePrice(
      wineName: row.isNotEmpty ? row[0]?.toString() ?? '' : '',
      wineVintage: row.length > 1 ? row[1]?.toString() ?? '' : '',
      price: row.length > 2 ? _parseDouble(row[2]) : 0.0,
      priceUnit: row.length > 3 ? row[3]?.toString() ?? 'EUR' : 'EUR',
      bottleSize: row.length > 4 ? row[4]?.toString() ?? '750ml' : '750ml',
      origin: row.length > 5 ? row[5]?.toString() ?? '' : '',
      country: row.length > 6 ? row[6]?.toString() ?? '' : '',
      score: row.length > 7 ? _parseDouble(row[7]) : 0.0,
      supplier: supplier,
    );
  }

  /// Convert to Excel row data
  List<dynamic> toExcelRow() {
    return [
      wineName,
      wineVintage,
      price,
      priceUnit,
      bottleSize,
      origin,
      country,
      score,
    ];
  }

  /// Helper method to safely parse double values
  static double _parseDouble(dynamic value) {
    if (value == null) return 0.0;
    if (value is double) return value;
    if (value is int) return value.toDouble();
    if (value is String) {
      return double.tryParse(value) ?? 0.0;
    }
    return 0.0;
  }

  @override
  String toString() {
    return 'WinePrice(name: $wineName, vintage: $wineVintage, price: $price, supplier: $supplier)';
  }

  /// Create a copy with modified fields
  WinePrice copyWith({
    String? wineName,
    String? wineVintage,
    double? price,
    String? priceUnit,
    String? bottleSize,
    String? origin,
    String? country,
    double? score,
    String? supplier,
  }) {
    return WinePrice(
      wineName: wineName ?? this.wineName,
      wineVintage: wineVintage ?? this.wineVintage,
      price: price ?? this.price,
      priceUnit: priceUnit ?? this.priceUnit,
      bottleSize: bottleSize ?? this.bottleSize,
      origin: origin ?? this.origin,
      country: country ?? this.country,
      score: score ?? this.score,
      supplier: supplier ?? this.supplier,
    );
  }
}
