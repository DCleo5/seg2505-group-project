package com.example.utaste.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.utaste.R;
import com.example.utaste.api.OpenFoodFactsService;
import com.example.utaste.database.RecipeDao;
import com.example.utaste.model.NutritionalInfo;
import com.example.utaste.model.User;
import com.example.utaste.util.QRCodeScanner;
import com.example.utaste.util.SessionManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddRecipeActivity extends AppCompatActivity {

    private static final int CAMERA_PERMISSION_SCAN = 2001;
    private static final int CAMERA_PERMISSION_PHOTO = 2002;
    private static final int CAMERA_CAPTURE_REQUEST = 3001;
    private static final int GALLERY_PICK_REQUEST = 3002;
    private static final int MIN_INGREDIENT_ROWS = 1;

    private EditText recipeNameInput;
    private ImageView recipeImageView;
    private Button takePhotoButton;
    private Button pickFromGalleryButton;

    private LinearLayout ingredientsContainer;
    private Button addIngredientRowButton;
    private TextView nutritionResultText;
    private Button calculateNutritionButton;
    private Button saveButton;
    private Button cancelButton;

    private final List<EditText> ingredientNameInputs = new ArrayList<>();
    private final List<EditText> ingredientPercentInputs = new ArrayList<>();
    private final List<TextView> ingredientBarcodeTexts = new ArrayList<>();
    private final List<String> ingredientBarcodes = new ArrayList<>();

    private int currentScanIndex = -1;

    private RecipeDao recipeDao;
    private SessionManager sessionManager;

    private String lastNutritionText = "";
    private final Map<String, NutritionalInfo> lastFetchedNutrition = new HashMap<>();

    // Totaux nutritionnels stockés en CHAMPS (plus de problème de "final")
    private double totalCarb, totalProt, totalLip, totalCal, totalFib, totalSalt;
    private int pendingNutritionRequests = 0;

    // Chemin du fichier image interne
    private String imagePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        recipeDao = new RecipeDao(this);
        sessionManager = SessionManager.getInstance(this);

        initViews();
        setupListeners();
        initIngredientRows();
    }

    private void initViews() {
        recipeNameInput = findViewById(R.id.recipeNameInput);
        recipeImageView = findViewById(R.id.recipeImageView);
        takePhotoButton = findViewById(R.id.takePhotoButton);

        pickFromGalleryButton = findViewById(R.id.pickFromGalleryButton);

        ingredientsContainer = findViewById(R.id.ingredientsContainer);
        addIngredientRowButton = findViewById(R.id.addIngredientRowButton);
        nutritionResultText = findViewById(R.id.ratioResultText);
        calculateNutritionButton = findViewById(R.id.calculateRatiosButton);
        saveButton = findViewById(R.id.saveButton);
        cancelButton = findViewById(R.id.cancelButton);
    }

    private void setupListeners() {
        addIngredientRowButton.setOnClickListener(v -> addIngredientRow());

        calculateNutritionButton.setOnClickListener(v -> calculateNutritionTotals());

        takePhotoButton.setOnClickListener(v -> checkCameraPermissionForPhoto());

        if (pickFromGalleryButton != null) {
            pickFromGalleryButton.setOnClickListener(v -> openGalleryPicker());
        }

        saveButton.setOnClickListener(v -> handleSave());
        cancelButton.setOnClickListener(v -> finish());
    }

    private void initIngredientRows() {
        for (int i = 0; i < MIN_INGREDIENT_ROWS; i++) {
            addIngredientRow();
        }
    }

    private void addIngredientRow() {
        android.view.View row = getLayoutInflater()
                .inflate(R.layout.item_ingredient_input, ingredientsContainer, false);

        EditText nameInput = row.findViewById(R.id.ingredientNameInput);
        EditText percentInput = row.findViewById(R.id.ingredientPercentInput);
        TextView barcodeText = row.findViewById(R.id.ingredientBarcodeText);
        Button scanButton = row.findViewById(R.id.ingredientScanButton);

        final int index = ingredientNameInputs.size();

        ingredientNameInputs.add(nameInput);
        ingredientPercentInputs.add(percentInput);
        ingredientBarcodeTexts.add(barcodeText);
        ingredientBarcodes.add(null);

        scanButton.setOnClickListener(v -> {
            currentScanIndex = index;
            checkCameraPermissionForScan();
        });

        ingredientsContainer.addView(row);
    }



    private void checkCameraPermissionForScan() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_SCAN
            );
        } else {
            QRCodeScanner.scan(this);
        }
    }

    private void checkCameraPermissionForPhoto() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_PHOTO
            );
        } else {
            openCameraForPhoto();
        }
    }

    private void openCameraForPhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, CAMERA_CAPTURE_REQUEST);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }

    private void openGalleryPicker() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_PICK_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length == 0
                || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this,
                    "Camera permission denied",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (requestCode == CAMERA_PERMISSION_SCAN) {
            if (currentScanIndex != -1) {
                QRCodeScanner.scan(this);
            }
        } else if (requestCode == CAMERA_PERMISSION_PHOTO) {
            openCameraForPhoto();
        }
    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //  Photo via caméra
        if (requestCode == CAMERA_CAPTURE_REQUEST && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap bitmap = (Bitmap) extras.get("data"); // mini-vignette
                if (bitmap != null) {
                    recipeImageView.setImageBitmap(bitmap);

                    // Sauvegarde en fichier interne
                    try {
                        File file = new File(getFilesDir(),
                                "recipe_" + System.currentTimeMillis() + ".jpg");
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.close();
                        imagePath = file.getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                        imagePath = null;
                    }
                }
            }
            return;
        }

        //  Image via galerie
        if (requestCode == GALLERY_PICK_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                recipeImageView.setImageURI(uri);
                imagePath = copyUriToInternalFile(uri);
            }
            return;
        }

        //  Scan QR / code-barres
        QRCodeScanner.parseResult(requestCode, resultCode, data,
                new QRCodeScanner.ScanCallback() {
                    @Override
                    public void onResult(String barcode) {
                        if (currentScanIndex >= 0
                                && currentScanIndex < ingredientBarcodes.size()) {
                            ingredientBarcodes.set(currentScanIndex, barcode);
                            ingredientBarcodeTexts.get(currentScanIndex)
                                    .setText("Code: " + barcode);
                        }
                    }

                    @Override
                    public void onCancelled() {
                        Toast.makeText(AddRecipeActivity.this,
                                "Scan cancelled",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private String copyUriToInternalFile(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            if (in == null) return null;

            File file = new File(getFilesDir(),
                    "gallery_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream out = new FileOutputStream(file);

            byte[] buffer = new byte[8192];
            int len;
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            in.close();
            out.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



    private void calculateNutritionTotals() {
        // reset totaux
        totalCarb = totalProt = totalLip = totalCal = totalFib = totalSalt = 0.0;
        pendingNutritionRequests = 0;
        lastFetchedNutrition.clear();

        List<Integer> validIndices = new ArrayList<>();
        List<Double> factors = new ArrayList<>();

        for (int i = 0; i < ingredientPercentInputs.size(); i++) {
            String percentStr = ingredientPercentInputs.get(i).getText().toString().trim();
            String barcode = ingredientBarcodes.get(i);

            if (!percentStr.isEmpty() && barcode != null) {
                try {
                    double percent = Double.parseDouble(percentStr);
                    if (percent <= 0) continue;
                    validIndices.add(i);
                    factors.add(percent / 100.0);
                } catch (NumberFormatException e) {
                    Toast.makeText(this,
                            "Invalid percentage at ingredient " + (i + 1),
                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        }

        if (validIndices.isEmpty()) {
            Toast.makeText(this,
                    "Enter at least one ingredient with % and scanned code",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        nutritionResultText.setText("Loading nutritional info...");
        OpenFoodFactsService service = new OpenFoodFactsService();
        pendingNutritionRequests = validIndices.size();

        for (int k = 0; k < validIndices.size(); k++) {
            int index = validIndices.get(k);
            double factor = factors.get(k);
            String barcode = ingredientBarcodes.get(index);

            service.fetchNutritionalInfo(barcode,
                    new OpenFoodFactsService.NutritionalInfoCallback() {
                        @Override
                        public void onSuccess(NutritionalInfo info) {
                            synchronized (AddRecipeActivity.this) {
                                totalCarb += info.getCarbohydrates() * factor;
                                totalProt += info.getProteins() * factor;
                                totalLip += info.getLipids() * factor;
                                totalCal += info.getCalories() * factor;
                                totalFib += info.getFibers() * factor;
                                totalSalt += info.getSalt() * factor;

                                lastFetchedNutrition.put(barcode, info);

                                pendingNutritionRequests--;
                                if (pendingNutritionRequests == 0) {
                                    displayNutritionTotals();
                                }
                            }
                        }

                        @Override
                        public void onError(String error) {
                            synchronized (AddRecipeActivity.this) {
                                pendingNutritionRequests--;
                                if (pendingNutritionRequests == 0) {
                                    displayNutritionTotals();
                                }
                            }
                            runOnUiThread(() ->
                                    Toast.makeText(AddRecipeActivity.this,
                                            "Error for one ingredient: " + error,
                                            Toast.LENGTH_SHORT).show());
                        }
                    });
        }
    }

    private void displayNutritionTotals() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Calories: %.2f kcal\n", totalCal));
        sb.append(String.format("Carbohydrates: %.2f g\n", totalCarb));
        sb.append(String.format("Proteins: %.2f g\n", totalProt));
        sb.append(String.format("Lipids: %.2f g\n", totalLip));
        sb.append(String.format("Fibers: %.2f g\n", totalFib));
        sb.append(String.format("Salt: %.2f g", totalSalt));

        lastNutritionText = sb.toString();
        runOnUiThread(() -> nutritionResultText.setText(lastNutritionText));
    }



    private void handleSave() {
        String name = recipeNameInput.getText().toString().trim();
        if (name.isEmpty()) {
            Toast.makeText(this,
                    "Please enter a recipe name",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // description à partir des ingrédients
        StringBuilder ingredientsBuilder = new StringBuilder();

        for (int i = 0; i < ingredientNameInputs.size(); i++) {
            String ingName = ingredientNameInputs.get(i).getText().toString().trim();
            String percent = ingredientPercentInputs.get(i).getText().toString().trim();
            String code = ingredientBarcodes.get(i);

            if (ingName.isEmpty() && percent.isEmpty() && code == null) {
                continue;
            }

            ingredientsBuilder.append("Ingredient ")
                    .append(i + 1)
                    .append(" : ");

            if (!ingName.isEmpty()) {
                ingredientsBuilder.append(ingName);
            } else {
                ingredientsBuilder.append("(no name)");
            }

            if (!percent.isEmpty()) {
                ingredientsBuilder.append(" - ")
                        .append(percent)
                        .append("%%");
            }

            if (code != null) {
                ingredientsBuilder.append(" - code: ")
                        .append(code);
            }

            ingredientsBuilder.append("\n");
        }

        String ingredientsDescription = ingredientsBuilder.toString().trim();

        StringBuilder fullDesc = new StringBuilder();
        if (!ingredientsDescription.isEmpty()) {
            fullDesc.append(ingredientsDescription);
        }

        if (lastNutritionText != null && !lastNutritionText.isEmpty()) {
            if (fullDesc.length() > 0) {
                fullDesc.append("\n\n");
            }
            fullDesc.append("Nutritional info:\n");
            fullDesc.append(lastNutritionText);
        }

        String finalDescription = fullDesc.toString();

        User chef = sessionManager.getCurrentUser();
        if (chef == null) {
            Toast.makeText(this,
                    "No logged-in chef found",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        long result = recipeDao.createRecipe(name, finalDescription, imagePath, chef.getId());

        if (result > 0) {
            Toast.makeText(this,
                    "Recipe created successfully",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this,
                    "Failed to create recipe",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
