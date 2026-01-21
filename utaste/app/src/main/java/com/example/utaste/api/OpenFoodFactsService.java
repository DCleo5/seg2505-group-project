package com.example.utaste.api;

import android.os.AsyncTask;
import android.util.Log;

import com.example.utaste.model.NutritionalInfo;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenFoodFactsService {

    private static final String TAG = "OpenFoodFactsService";
    private static final String BASE_URL =
            "https://world.openfoodfacts.org/api/v0/product/%s.json";

    // Callback utilisé par RecipeDetailActivity
    public interface NutritionalInfoCallback {
        void onSuccess(NutritionalInfo info);
        void onError(String message);
    }

    // Appel public depuis l’activité : new OpenFoodFactsService().fetchNutritionalInfo(...)
    public void fetchNutritionalInfo(String barcode, NutritionalInfoCallback callback) {
        new FetchTask(callback).execute(barcode);
    }

    // ---------- AsyncTask réseau ----------

    private static class FetchTask extends AsyncTask<String, Void, NutritionalInfo> {

        private final NutritionalInfoCallback callback;
        private String errorMessage;

        FetchTask(NutritionalInfoCallback callback) {
            this.callback = callback;
        }

        @Override
        protected NutritionalInfo doInBackground(String... params) {
            String barcode = params[0];
            HttpURLConnection conn = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(String.format(BASE_URL, barcode));
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);

                int responseCode = conn.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    errorMessage = "HTTP error: " + responseCode;
                    return null;
                }

                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream())
                );
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                return parseNutritionalInfo(sb.toString());

            } catch (Exception e) {
                Log.e(TAG, "Error while calling OpenFoodFacts", e);
                errorMessage = e.getMessage();
                return null;
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (Exception ignored) {}
                if (conn != null) conn.disconnect();
            }
        }

        @Override
        protected void onPostExecute(NutritionalInfo info) {
            if (info != null) {
                callback.onSuccess(info);
            } else {
                if (errorMessage == null ||
                        errorMessage.trim().isEmpty()) {
                    errorMessage = "Aucune information nutritionnelle trouvée pour ce code-barres.";
                }
                callback.onError(errorMessage);
            }
        }

        // --------- Parsing JSON OpenFoodFacts ---------

        private NutritionalInfo parseNutritionalInfo(String json) throws Exception {
            JsonObject root = JsonParser.parseString(json).getAsJsonObject();

            int status = root.has("status") ? root.get("status").getAsInt() : 0;
            if (status != 1) {
                throw new Exception("Produit introuvable.");
            }

            JsonObject product = root.getAsJsonObject("product");
            if (product == null || !product.has("nutriments")) {
                throw new Exception("Aucune donnée de nutriments.");
            }

            JsonObject nutriments = product.getAsJsonObject("nutriments");

            // Les clés viennent directement d’OpenFoodFacts (100g)
            double carbohydrates = getNutrientValue(nutriments, "carbohydrates_100g");
            double proteins      = getNutrientValue(nutriments, "proteins_100g");
            double lipids        = getNutrientValue(nutriments, "fat_100g");
            double calories      = getNutrientValue(nutriments, "energy-kcal_100g");
            double fibers        = getNutrientValue(nutriments, "fiber_100g");
            double salt          = getNutrientValue(nutriments, "salt_100g");

            // Tu peux ajouter d’autres champs ici si tu en as besoin

            return new NutritionalInfo(
                    carbohydrates,
                    proteins,
                    lipids,
                    calories,
                    fibers,
                    salt
            );
        }
    }

    // Petit helper pour lire un nutriment de façon sécuritaire
    private static double getNutrientValue(JsonObject nutriments, String key) {
        try {
            if (nutriments.has(key) && !nutriments.get(key).isJsonNull()) {
                return nutriments.get(key).getAsDouble();
            }
        } catch (Exception e) {
            Log.w(TAG, "Nutrient not found or invalid: " + key);
        }
        return 0.0;
    }
}
