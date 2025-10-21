import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'screens/spreadsheet_view.dart';
import 'providers/wine_data_provider.dart';

void main() {
  runApp(const CompareWineApp());
}

/// Main application widget
class CompareWineApp extends StatelessWidget {
  const CompareWineApp({super.key});

  @override
  Widget build(BuildContext context) {
    return ChangeNotifierProvider(
      create: (_) => WineDataProvider(),
      child: MaterialApp(
        title: 'Compare Wine',
        debugShowCheckedModeBanner: false,
        theme: ThemeData(
          colorScheme: ColorScheme.fromSeed(
            seedColor: Colors.deepPurple,
            brightness: Brightness.light,
          ),
          useMaterial3: true,
          appBarTheme: const AppBarTheme(
            centerTitle: false,
            elevation: 2,
          ),
          cardTheme: CardTheme(
            elevation: 2,
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(8),
            ),
          ),
          elevatedButtonTheme: ElevatedButtonThemeData(
            style: ElevatedButton.styleFrom(
              padding: const EdgeInsets.symmetric(horizontal: 16, vertical: 12),
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.circular(8),
              ),
            ),
          ),
        ),
        home: const SpreadsheetView(),
      ),
    );
  }
}
