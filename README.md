# Pokke 🎵

App de música personalizable para Android (estilo Apple Music / Spotify) con Jetpack Compose.

## Características incluidas en esta base

- ✅ Kotlin + Jetpack Compose + Material 3
- ✅ Tema oscuro con estilo Liquid Glass (blur / glassmorphism)
- ✅ Login / Registro (UI lista, Supabase por conectar)
- ✅ Emblema **Owner** automático para `lunethos.dev@gmail.com`
- ✅ Código secreto de Owner: `mikuthegoat.`
- ✅ Búsqueda de canciones vía API Cloudflare (`/api/search?q=`)
- ✅ Descarga de MP3 con progreso en tiempo real
- ✅ Servicio de reproducción en segundo plano (Media3 + notificación)
- ✅ Navegación inferior (Inicio / Buscar / Biblioteca / Perfil)
- ✅ Keystore de firma incluido (`pokke-release.keystore`)
- ✅ GitHub Actions listo para generar APK firmado

## Package

`com.poke.music`

## Requisitos

- Android Studio Ladybug o superior
- JDK 17
- minSdk 26 / targetSdk 35

## Cómo abrir el proyecto

1. Descomprime el ZIP
2. Abre la carpeta `Pokke` en Android Studio
3. Espera a que Gradle sincronice
4. Reemplaza en `app/build.gradle.kts` las constantes:
   - `SUPABASE_URL`
   - `SUPABASE_ANON_KEY`
5. Ejecuta en un emulador o dispositivo

## Keystore (firma)

- Archivo: `pokke-release.keystore`
- Alias: `pokke`
- Contraseña (store y key): `pokke2026secure`

**Importante:** Cambia la contraseña en producción y guárdala en GitHub Secrets:

- `POKKE_KEYSTORE_BASE64` → keystore codificado en base64
- `POKKE_STORE_PASSWORD`
- `POKKE_KEY_PASSWORD`

Para generar el base64:
```bash
base64 -w 0 pokke-release.keystore > keystore.b64
```

## API de música

Base temporal:
```
https://sol-deeply-feature-administration.trycloudflare.com/api/search?q=NOMBRE
```

Cuando el link de Cloudflare cambie, se podrá actualizar desde el Panel Admin (próxima versión).

## Estructura principal

```
app/src/main/java/com/poke/music/
├── ui/
│   ├── theme/          # Colores, tipografía, tema
│   ├── screens/        # Pantallas (auth, home, search…)
│   ├── navigation/     # NavGraph
│   └── components/
├── data/
│   ├── model/          # Song, Profile…
│   ├── remote/         # MusicApi
│   └── repository/
├── service/            # MusicPlaybackService
└── util/               # BadgeHelper
```

## Próximas fases

1. Conectar Auth real con Kokoro Supabase
2. Reproductor completo + cola + lyrics
3. Panel Admin (cambiar URL del servidor + anuncios)
4. Chats, amigos y grupos
5. Widgets (pequeño, grande, lyrics)
6. Programa de artistas
7. Customización total de fuentes/colores/banner

## Autor

Lunethos · lunethos.dev@gmail.com
