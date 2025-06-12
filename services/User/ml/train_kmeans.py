import os
import pandas as pd
from sklearn.cluster import KMeans
from sklearn.preprocessing import StandardScaler
import joblib
import mysql.connector

# Configuration de la base de données
db_config = {
    'host': 'localhost',
    'user': 'root',
    'password': '',
    'database': 'UserPI'
}

# Établir la connexion à la base de données
cnx = mysql.connector.connect(**db_config)

# Récupérer les données utilisateur
query = """
    SELECT 
        specialty,
        sector,
        paid,
        verified
    FROM User
"""

df = pd.read_sql(query, cnx)
cnx.close()

# Liste des catégories attendues (identique au traitement original)
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

# Création des colonnes one-hot pour specialty
for cat in specialty_categories:
    df[f'specialty_{cat}'] = (df['specialty'] == cat).astype(int)

# Création des colonnes one-hot pour sector
for cat in sector_categories:
    df[f'sector_{cat}'] = (df['sector'] == cat).astype(int)

# Suppression des colonnes originales
df = df.drop(columns=['specialty', 'sector'])

# Gestion des valeurs manquantes
df['paid'] = df['paid'].fillna(-1).astype(int)
df['verified'] = df['verified'].fillna(-1).astype(int)

# Sélection des colonnes numériques
df = df.select_dtypes(include=['number'])

# Normalisation des données
scaler = StandardScaler()
scaled_data = scaler.fit_transform(df)

# Entraînement du modèle KMeans
kmeans = KMeans(n_clusters=3, random_state=42)
kmeans.fit(scaled_data)

# Sauvegarde des artefacts
output_dir = os.path.join("..", "src", "main", "resources")
os.makedirs(output_dir, exist_ok=True)

joblib.dump(kmeans, os.path.join(output_dir, 'kmeans_model.pkl'))
joblib.dump(scaler, os.path.join(output_dir, 'scaler.pkl'))

print("Modèle et scaler sauvegardés avec succès!")