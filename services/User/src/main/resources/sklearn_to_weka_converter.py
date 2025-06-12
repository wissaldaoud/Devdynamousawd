import os
import numpy as np
import pandas as pd
import joblib
import pickle
import json

# Charger le modèle scikit-learn
def load_sklearn_model(model_path, scaler_path):
    kmeans_model = joblib.load(model_path)
    scaler = joblib.load(scaler_path)
    return kmeans_model, scaler

# Convertir le modèle en format utilisable par Java
def convert_to_java_compatible(kmeans_model, scaler, feature_names):
    try:
        # Extraire les informations du modèle
        centroids = kmeans_model.cluster_centers_
        n_clusters = kmeans_model.n_clusters

        # Créer un dictionnaire avec les informations essentielles
        model_info = {
            "n_clusters": n_clusters,
            "centroids": centroids.tolist(),
            "feature_names": feature_names,
            "inertia": float(kmeans_model.inertia_ if hasattr(kmeans_model, 'inertia_') else 0)
        }

        # Enregistrer le modèle au format JSON
        output_path = os.path.join("kmeans_model.json")
        with open(output_path, 'w') as f:
            json.dump(model_info, f, indent=2)

        # Enregistrer également le modèle au format pickle (comme backup)
        pickle_path = os.path.join("kmeans_model.model")
        with open(pickle_path, 'wb') as f:
            pickle.dump(kmeans_model, f)

        # Enregistrer le scaler séparément
        scaler_path = os.path.join("scaler.model")
        with open(scaler_path, 'wb') as f:
            pickle.dump(scaler, f)

        print(f"Modèle converti et enregistré à {output_path}")
        print(f"Modèle pickle enregistré à {pickle_path}")
        print(f"Scaler enregistré à {scaler_path}")
        return True

    except Exception as e:
        print(f"Erreur lors de la conversion du modèle: {e}")
        return False

if __name__ == "__main__":
    # Chemins des fichiers
    model_path = os.path.join("kmeans_model.pkl")
    scaler_path = os.path.join("scaler.pkl")

    # Charger le modèle et le scaler
    kmeans_model, scaler = load_sklearn_model(model_path, scaler_path)

    # Liste des noms de caractéristiques basée sur le modèle train_kmeans.py
    specialty_categories = [
        'Data Science',
        'Finance',
        'Informatique',
        'Marketing',
        'Mathématiques'
    ]

    sector_categories = [
        'Finance',
        'Santé',
        'Technologie',
        'Éducation',
        'Énergie'
    ]

    # Définir les noms des caractéristiques dans le même ordre que dans train_kmeans.py
    feature_names = ['paid', 'verified']

    # Ajouter les colonnes one-hot pour specialty
    for cat in specialty_categories:
        feature_names.append(f'specialty_{cat}')

    # Ajouter les colonnes one-hot pour sector
    for cat in sector_categories:
        feature_names.append(f'sector_{cat}')

    # Convertir le modèle
    convert_to_java_compatible(kmeans_model, scaler, feature_names)