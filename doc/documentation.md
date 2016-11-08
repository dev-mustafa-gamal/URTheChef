Cookbook Documentation
======================


## Intro

This is a documentation for Cookbook app created by [Robo Templates](http://robotemplates.com/). Cookbook is a native Android application. You can find here useful info about configuring, customizing, building and publishing the app.

* [Cookbook on Codecanyon](http://codecanyon.net/item/cookbook-recipe-app-for-android/10747654)
* [Video preview on YouTube](https://www.youtube.com/watch?v=dxpd-ryoTH4)
* [Live demo on Google Play](https://play.google.com/store/apps/details?id=com.robotemplates.cookbook)


## Features

* Support for Ice Cream Sandwich (Android 4.0.3) and newer
* Developed with Android Studio & Gradle
* Material design following Android Design Guidelines
* Eight color themes (blue, brown, carrot, gray, green, indigo, red, yellow)
* Animations and effects
* Animated action bar
* Animated floating action button
* Parallax scrolling effect
* Quick return effect
* Ripple effect
* Navigation drawer menu with categories
* List of recipes
* Search for recipe with suggestion
* Favorite recipes
* Recipe detail screen (intro, ingredients, instruction)
* Ingredients check list
* Share recipe or shopping list
* Open web link of the recipe
* Kitchen timer
* Recalculate quantity of ingredients by servings
* Convert calories to joules
* About dialog
* Rate app on Google Play
* Data (categories, recipes, ingredients) is stored in local SQLite database
* Images can be loaded from the Internet or locally
* Caching images
* App works in offline mode
* Runtime permissions
* Google Analytics
* AdMob
* Responsive design and tablet support (portrait, landscape, handling orientation change)
* Support for high-resolution displays (xxxhdpi)
* Multi-language support
* Possibility to build the project without Android Studio / Eclipse (using Gradle & Android SDK)
* Easy configuration
* Well documented
* Top quality clean code created by experienced senior Android developer
* Free support


## Android Studio & SDK

This chapter describes how to install Android Studio and Android SDK. You don't have to install Android Studio, but it's better. The project can be built without Android Studio, using Gradle and Android SDK. Gradle is a build system used for building final APK file.

1. Install [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html)
2. Install [Android SDK](https://developer.android.com/sdk/index.html)
3. Run Android SDK Manager and [download necessary SDK packages](https://developer.android.com/sdk/installing/adding-packages.html), make sure that you have installed Android SDK Tools, Android SDK Platform-tools, Android SDK Build-tools, Android Support Repository, Android Support Library, Google Play services and Google USB Driver
4. Install [Android Studio](https://developer.android.com/sdk/index.html)
5. Now you should be able to open/edit the Android project and build APK
6. You can also install [Genymotion](http://www.genymotion.com/) - fast Android emulator


## Project Structure

Project has the following structure (directories are marked by square braces):

- [doc] - documentation
- [extras] - contains extras
- [extras]/[keystore]
- [extras]/[keystore]/cookbook.jks - keystore certificate for signing APK
- [extras]/[keystore]/cookbook.properties - alias and password for keystore
- [gradle]
- [gradle]/[wrapper] - Gradle Wrapper
- [mobile] - main module
- [mobile]/[libs] - contains 3rd party libraries (not used)
- [mobile]/[src] - contains source code
- [mobile]/[src]/[main]
- [mobile]/[src]/[main]/[assets] - asset files (prepopulated database, images)
- [mobile]/[src]/[main]/[java] - java sources
- [mobile]/[src]/[main]/[res] - xml resources, drawables
- [mobile]/[src]/[main]/AndroidManifest.xml - manifest file
- [mobile]/build.gradle - main build script
- [mobile]/proguard-rules.pro - Proguard config (not used)
- .gitignore - Gitignore file
- build.gradle - parent build script
- gradle.properties - build script properties containing path to keystore
- gradlew - Gradle Wrapper (Unix)
- gradlew.bat - Gradle Wrapper (Windows)
- README.md - readme file
- settings.gradle - build settings containing list of modules

Java packages:

- com.robotemplates.cookbook - contains application class and main config class
- com.robotemplates.cookbook.activity - contains activities representing screens
- com.robotemplates.cookbook.adapter - contains all adapters
- com.robotemplates.cookbook.content - contains content provider for search suggestions
- com.robotemplates.cookbook.database - contains database helper and tools for managing asynchronous database calls
- com.robotemplates.cookbook.database.dao - database access objects
- com.robotemplates.cookbook.database.data - data model wrapper
- com.robotemplates.cookbook.database.model - database models representing SQL tables
- com.robotemplates.cookbook.database.query - database queries
- com.robotemplates.cookbook.dialog - contains dialogs
- com.robotemplates.cookbook.fragment - contains fragments with main application logic
- com.robotemplates.cookbook.listener - contains event listeners
- com.robotemplates.cookbook.utility - contains utilities
- com.robotemplates.cookbook.view - contains custom views, layouts, decorations and other tools for working with views


## Configuration

This chapter describes how to configure the project to be ready for publishing. All these steps are very important!


### 1. Import

Unzip the package and import/open the project in Android Studio. Choose "Import project" on Quick Start screen and select "cookbook-x.y.z" directory.

If you want, you can build and run the app right away to test it. Connect your device or [emulator](http://developer.android.com/tools/devices/index.html) to your computer. Make sure you have installed all necessary [drivers](http://developer.android.com/tools/extras/oem-usb.html) for your Android device and you also enabled [USB debugging in Developer options](http://developer.android.com/tools/device.html). To build and run the app in Android Studio, just click on Main menu -> Run -> Run 'mobile'. Choose a running device and confirm.


### 2. Rename Package Name

[The package name](http://developer.android.com/guide/topics/manifest/manifest-element.html#package) serves as a unique identifier for the application. It is also unique in the Google Play store. Once you publish your application, you cannot change the package name. The package name defines your application's identity, so if you change it, then it is considered to be a different application and users of the previous version cannot update to the new version. Default package name is "com.robotemplates.cookbook" so you have to rename it to something else.

1. Create new package in _java_ directory, e.g. "com.mycompany.myapp". In Project window, right click on _mobile/src/main/java_ directory -> New -> Package. Enter your package name and confirm.
2. Select all packages and classes in "com.robotemplates.cookbook" and move (drag) them to the new package. Confirm by click on "Refactor" button and then "Do Refactor".
3. Delete the old package "com.robotemplates.cookbook".
4. Open _mobile/src/main/AndroidManifest.xml_ and rename the package name. Select package name "com.robotemplates.cookbook" and rewrite it to your new package name, e.g. "com.mycompany.myapp".
5. Clean the project. Main menu -> Build -> Clean Project.
6. Replace all occurrences of "com.robotemplates.cookbook" for a new package name, e.g. "com.mycompany.myapp". Right click on _mobile_ directory -> Replace in Path -> set old and new package names, "Case sensitive" to true -> Find -> Replace -> All Files.
7. Clean the project again. Main menu -> Build -> Clean Project.
8. Synchronize the project. Main menu -> Tools -> Android -> Sync Project with Gradle Files.

**Note:** If you see "Activity class does not exist" error, restart Android Studio.


### 3. Rename Application Name

Open _mobile/src/main/res/values/strings.xml_ and change "Cookbook" to your own name. Change _app\_name_ and _drawer\_title_ strings.


### 4. Create Launcher Icon

Right click on _mobile/src/main/res_ directory -> New -> Image Asset -> Asset type "Launcher Icons", Resource name "ic\_launcher", create the icon as you wish, you can set clipart, text, shape, color etc. -> Next -> Finish.

You can also change the icon by replacing _ic\_launcher.png_ file in _mipmap-mdpi_, _mipmap-hdpi_, _mipmap-xhdpi_, _mipmap-xxhdpi_, _mipmap-xxxhdpi_ directories. See [Android Cheatsheet for Graphic Designers](http://petrnohejl.github.io/Android-Cheatsheet-For-Graphic-Designers/#screen-densities-and-icon-dimensions) for correct launcher icon dimensions.

**Tip:** Another possibility is to create launcher icons using [Android Asset Studio](http://romannurik.github.io/AndroidAssetStudio/icons-launcher.html).


### 5. Choose Color Theme

Open _mobile/src/main/AndroidManifest.xml_ and change value of `application.android:theme` attribute. There are 8 themes you can use:

* Theme.Cookbook.Blue
* Theme.Cookbook.Brown
* Theme.Cookbook.Carrot
* Theme.Cookbook.Gray
* Theme.Cookbook.Green
* Theme.Cookbook.Indigo
* Theme.Cookbook.Red
* Theme.Cookbook.Yellow

You also have to modify MainActivity's theme. Main Activity uses a special theme with transparent status bar because of navigation drawer status overlay effect. It is an `activity.android:theme` attribute. Choose one of these themes:

* Theme.Cookbook.TransparentStatusBar.Blue
* Theme.Cookbook.TransparentStatusBar.Brown
* Theme.Cookbook.TransparentStatusBar.Carrot
* Theme.Cookbook.TransparentStatusBar.Gray
* Theme.Cookbook.TransparentStatusBar.Green
* Theme.Cookbook.TransparentStatusBar.Indigo
* Theme.Cookbook.TransparentStatusBar.Red
* Theme.Cookbook.TransparentStatusBar.Yellow


### 6. Prepare Database and Images

Data (categories, recipes, ingredients) is stored in local SQLite database. Prepopulated database with recipes is stored in _mobile/src/main/assets/cookbook.db_. This prepopulated database is automatically copied on device storage on first run of the application and also if the database is updated (see below for more info about database update). Database is in SQLite 3.0 format and has the following structure (SQL script):

```sql
CREATE TABLE `categories` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `name` VARCHAR , `image` VARCHAR );
CREATE TABLE `recipes` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `category_id` BIGINT , `name` VARCHAR , `intro` VARCHAR , `instruction` VARCHAR , `image` VARCHAR , `link` VARCHAR , `time` INTEGER , `servings` INTEGER , `calories` INTEGER , `favorite` SMALLINT );
CREATE INDEX `recipes_category_idx` ON `recipes` ( `category_id` );
CREATE TABLE `ingredients` (`id` INTEGER PRIMARY KEY AUTOINCREMENT , `recipe_id` BIGINT , `name` VARCHAR , `quantity` FLOAT , `unit` VARCHAR );
CREATE INDEX `ingredients_recipe_idx` ON `ingredients` ( `recipe_id` );
```

As you can see, there are 3 SQL tables (categories, recipes, ingredients) and 2 indexes (category\_id, recipe\_id) which are also foreign keys. This database schema corresponds to database models in the code. You can find models in _com.robotemplates.cookbook.database.model_ package.

This app contains a prepopulated database with demo data. Open database file _mobile/src/main/assets/cookbook.db_ in any SQLite editor and modify data in the database as you need. You can add/remove/edit recipes, categories and ingredients. There are many [SQLite editors](http://www.sqlite.org/cvstrac/wiki?p=ManagementTools). We recommend [SQLite Studio](http://sqlitestudio.pl/) because it is free, open source, cross-platform, portable and intuitive. If you are working with SQLite Studio, don't forget to commit changes. Don't modify structure of the database, modify only data! Database tables have following columns:

categories:
* id (integer) - Unique primary key
* name (string) - Category name
* image (string) - URL of the category image. This field is optional and if it is empty or null, no category image is shown. URL should be in this format: _assets://categories/mycategory.png_. It points to _mobile/src/main/assets/categories_ folder where all category images should be stored.

recipes:
* id (integer) - Unique primary key
* category_id (integer) - Foreign key pointing to category id
* name (string) - Recipe name
* intro (string) - Introduction text on recipe detail screen. This field is optional and if it is empty or null, no text is shown.
* instruction (string) - Main instruction text for the recipe
* image (string) - URL of the recipe image. Image can be loaded from the Internet (URL with standard HTTP protocol) or locally from assets. Local URL should be in this format: _assets://recipes/myrecipe.jpg_. It points to _mobile/src/main/assets/recipes_ folder where all local recipe images should be stored.
* link (string) - URL of the web page. This field is optional and if it is empty or null, no web link is shown in the menu.
* time (integer) - Cooking time in minutes. This field is optional and if it is empty or null, no time is shown.
* servings (integer) - Number of servings. This field is optional and if it is empty or null, no servings info is shown.
* calories (integer) - Number of calories in kcal for 100 g of serving. This field is optional and if it is empty or null, no calories info is shown.
* favorite (boolean) - True/false value if the recipe is favorite. This field should stay 0 by default. This is the only column modified by the app. All other columns are read only.

ingredients:
* id (integer) - Unique primary key
* recipe_id (integer) - Foreign key pointing to recipe id
* name (string) - Ingredient name
* quantity (float) - Quantity of the ingredient. This field is optional and if it is empty or null, no quantity is shown.
* unit (string) - Physical unit of the quantity. This field is optional and if it is empty or null, no unit is shown.

There are two special categories: "All recipes" and "Favorites". Keep in mind that these categories are automatically added to the menu and don't have to be in the database. Categories are ordered by _id_, recipes are ordered alphabetically by _name_ and ingredients are ordered by _id_. Search query is looking for a match in _name_, _intro_ and _instruction_ fields. Searching is case insensitive.

If you modify prepopulated database in _assets_ folder, internal database on device storage will not be updated automatically. If you make any change in the prepopulated database, you have to increment database version. Open configuration file _mobile/src/main/java/com/robotemplates/cookbook/CookbookConfig.java_ and increment number in _DATABASE\_VERSION_ constant. Database helper detects that database data has been changed and copy the prepopulated database on device storage so data in the app will be updated. You have to increment database version every time when you want to publish a new build on Google Play and you have changed the data in prepopulated database.

Name of the prepopulated database is defined in configuration file _mobile/src/main/java/com/robotemplates/cookbook/CookbookConfig.java_ in _DATABASE\_NAME_ constant. Database file name should correspond to the file in _mobile/src/main/assets_ directory. The database file should be stored in this directory and not in any sub-directory.

This paragraph will describe in more detail how it works. Note that there are 2 databases. The first one is prepopulated database which is located in _mobile/src/main/assets/cookbook.db_. This database has all data about your categories, recipes and ingredients. If you want to add/remove/edit data in your app, you should modify this database. The important thing is, that this database is baked in the APK file so when you build the APK file, this database is part of it. Prepopulated database is not directly used by your app. It is just a mirror and when you run the app for a first time (or after update), prepopulated database is copied into device. The second database is the real/internal database which is actually used directly by app when you run it. This database is stored in the device and it is automatically created from the prepopulated database – it is cloned/copied from it. If you want to use more translations in your app, you have to have more prepopulated databases – for each language. For example: default _cookbook.db_, _fr\_cookbook.db_ for French, _de\_cookbook.db_ for German etc. See "Multi-language support" chapter below for more info.


### 7. Setup Google Analytics

Open configuration file _mobile/src/main/java/com/robotemplates/cookbook/CookbookConfig.java_ and set value of `ANALYTICS_TRACKING_ID` constant to your own UA tracking id. Leave the constant empty if you don't want to use Google Analytics.


### 8. Setup AdMob

Open configuration file _mobile/src/main/java/com/robotemplates/cookbook/CookbookConfig.java_ and set values of `ADMOB_UNIT_ID_RECIPE_LIST` and `ADMOB_UNIT_ID_RECIPE_DETAIL` constants to your own unit ids (banner ids). Leave these constants empty if you don't want to use AdMob.

You can also specify your test device id in `ADMOB_TEST_DEVICE_ID` constant and use test mode when you are debugging the app. Requesting test ads is recommended when you are testing your application on a real device so you do not request invalid impressions. You can find your hashed device id in Android Monitor (Logcat) output by requesting an ad when debugging on your device. Open Android Monitor (Logcat) in Android Studio and look for "To get test ads on this device, call adRequest.addTestDevice("XXXXXX…");". You can use filter/search to find this information in the log. That XXX string is the hashed device id. This applies only to real devices, not emulators. Emulators are considered as test devices by default so you don't have to care about it.


### 9. Create Signing Keystore

You need to create your own keystore to [sign an APK file](http://developer.android.com/tools/publishing/app-signing.html) before [publishing on Google Play](http://developer.android.com/distribute/googleplay/start.html). You can create the keystore via [keytool utility](http://docs.oracle.com/javase/7/docs/technotes/tools/solaris/keytool.html) which is part of Java JDK. On Windows, you can find it usually in _C:\Program Files\Java\jdkX.Y.Z\bin_. On Mac, you can find it usually in _/Library/Java/JavaVirtualMachines/jdkX.Y.Z/Contents/Home/bin_.

1. Run following command: `keytool -genkey -v -keystore cookbook.jks -alias <your_alias> -keyalg RSA -keysize 2048 -validity 36500` where `<your_alias>` is your alias name. For example your company name or app name.
2. Copy new _cookbook.jks_ file into _extras/keystore_ directory.
3. Open _extras/keystore/cookbook.properties_ and set keystore alias and passwords.
4. Done. Remember that _cookbook.jks_ and _cookbook.properties_ are automatically read by Gradle script when creating a release APK via _assembleRelease_ command. Paths to these files are defined in _gradle.properties_.

**Note:** You can also create a keystore directly in Android Studio. Main menu -> Build -> Generate Signed APK -> Create new. If you do it this way, don't forget for step 2 and 3 above. Keystore name has to be _cookbook.jks_ and should be stored in _extras/keystore_ directory.


## Customization

This chapter describes optional customizations of the app.


### Custom Colors and Icons

You can customize colors in _mobile/src/main/res/values/colors.xml_.

There are 11 category icons. If you need to create an icon for category, it is recommended to use [Android Asset Studio](http://romannurik.github.io/AndroidAssetStudio/index.html). See [Android Cheatsheet for Graphic Designers](http://petrnohejl.github.io/Android-Cheatsheet-For-Graphic-Designers/#screen-densities-and-icon-dimensions) for correct icon dimensions. Use icons with highest DPI.


### Custom Banner Logo in Navigation Drawer Menu

There is a green table cloth texture shown in the navigation drawer menu. You can easily change this texture by replacing _banner.png_ file in _drawable-xxhdpi_ directory.


### About Dialog

If you want to change the text in About dialog, just open _mobile/src/main/res/values/strings.xml_ and edit _dialog\_about\_message_ string. Note that this text is in HTML format and it can also contains links.


### Multi-Language Support

Create a new directory _mobile/src/main/res/values-xx_ where _xx_ is an [ISO 639-1 code](http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) of the language you want to translate. For example "values-es" for Spanish, "values-fr" for French, "values-de" for German etc. Copy strings.xml from _mobile/src/main/res/values_ into the new directory. Now you can translate texts for specific languages. The language is automatically determined by the system device settings. If there is no match with _values-xx_ language, default language in _mobile/src/main/res/values_ is selected. See [Localizing with Resources](http://developer.android.com/guide/topics/resources/localization.html) for more info.

For database translation, you have to have more prepopulated databases for each language. For example: default _cookbook.db_, _fr\_cookbook.db_ for French, _de\_cookbook.db_ for German etc. Name of the database file has to be in this format: `<iso_code>_cookbook.db` where `<iso_code>` is an [ISO 639-1 code](http://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) of the language you want to translate. The language is automatically determined by the system device settings. If there is no match with the language ISO code, default prepopulated database _cookbook.db_ is used. All database files should be stored in _mobile/src/main/assets_ directory.


## Building & Publishing

This chapter describes how to build APK file using Gradle and prepare app for publishing. Android Studio uses Gradle for building Android applications.

You don't need to install Gradle on your system, because there is a [Gradle Wrapper](http://www.gradle.org/docs/current/userguide/gradle_wrapper.html). The wrapper is a batch script on Windows, and a shell script for other operating systems. When you start a Gradle build via the wrapper, Gradle will be automatically downloaded and used to run the build.

1. Open the project in Android Studio
2. Open configuration file _mobile/src/main/java/com/robotemplates/cookbook/CookbookConfig.java_ and check if everything is setup correctly
3. Open main build script _mobile/build.gradle_ and check version constants
4. Run `gradlew assemble` in Android Studio terminal, make sure you are running the command from root directory of the project
5. After the build is finished, APK file should be available in _mobile/build/outputs/apk_ directory

**Note:** You will also need a "local.properties" file to set the location of the SDK in the same way that the existing SDK requires, using the "sdk.dir" property. Example of "local.properties" on Windows: `sdk.dir=C:\\adt-bundle-windows\\sdk`. Alternatively, you can set an environment variable called "ANDROID\_HOME". Android Studio will take care of it.

**Tip:** Command `gradlew assemble` builds both - debug and release APK. You can use `gradlew assembleDebug` to build debug APK. You can use `gradlew assembleRelease` to build release APK. Debug APK is signed by debug keystore. Release APK is signed by your own keystore, stored in _/extras/keystore_ directory.

**Signing process:** Keystore passwords are automatically loaded from property file during building the release APK. Path to this file is defined in "keystore.properties" property in "gradle.properties" file. If this property or the file does not exist, user is asked for passwords explicitly.


### Versioning

Open the main build script _mobile/build.gradle_. There are 4 important constants for defining version code and version name.

* VERSION\_MAJOR
* VERSION\_MINOR
* VERSION\_PATCH
* VERSION\_BUILD

Keep in mind that versions have to be incremental. See [Versioning Your Applications](http://developer.android.com/tools/publishing/versioning.html#appversioning) in Android documentation for more info.


## Dependencies

* [Android Support Library](http://developer.android.com/tools/support-library/index.html)
* [FloatingActionButton](https://github.com/makovkastar/FloatingActionButton)
* [Google Play Services](http://developer.android.com/google/play-services/index.html)
* [OrmLite](http://ormlite.com/)
* [RecyclerView Multiselect](https://github.com/bignerdranch/recyclerview-multiselect)
* [StickyScrollViewItems](https://github.com/emilsjolander/StickyScrollViewItems)
* [Universal Image Loader](https://github.com/nostra13/Android-Universal-Image-Loader)


## Credits

Following images are used in the demo app:

* Goulash Soup, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/elsiehui/14706147387
* Apple Pie, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/jeffreyww/15160498676
* Perfect Sandwiches, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/jeffreyww/5696849258
* Orange Juice With Ginger, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/breville/8735921698
* Oven Roasted Potato Fries, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/gudlyf/3862654181
* Baked Biscuits, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/epw/2675532274
* Belgian Waffle, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/ralphandjenny/5434298641
* English Breakfast, [CC BY-ND 2.0](https://creativecommons.org/licenses/by-nd/2.0/), https://www.flickr.com/photos/dewolfert/3866881455
* Eggs With Crab Dip, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/jeffreyww/5291277352
* Hot Gingered Prawns, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/breville/8735236955
* Rings Calamari, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/saechang/4509691209
* Sushi Rolls, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/sql_samson/3977310279
* Tuna Nachos, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/dinesarasota/6791316679
* Curried Asparagus and Kaffir Lime Soup, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/dougbeckers/12870205414
* Matzoh Ball Soup, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/stuart_spivack/298130263
* Potato Soup, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/whitneyinchicago/4072293909
* Split Pea Soup, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/cogdog/11579080605
* Vegetable Soup, [CC BY-ND 2.0](https://creativecommons.org/licenses/by-nd/2.0/), https://www.flickr.com/photos/9439733@N02/2088701805
* Halushki, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/avlxyz/4567416647
* Orange Chicken With Bacon Fried Rice, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/jeffreyww/16241424778
* Posole and Quesadillas, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/jeffreyww/15865540490
* Spaghetti Carbonara, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/jeffreyww/13726596053
* Tuna Steak, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/elwillo/5993250162
* Summer Salad With Tomatoes and Basil, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/jasonsewall/6090556228
* Cupcake, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/jenumfamily/6911184740
* Key Lime Pie, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/ralphandjenny/5941648290
* Lemon Berry Trifle, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/brooke/2364980971
* Meringue Cake With Red Currant Curd Filling, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/69668444@N03/7801333926
* Strawberry Cake, [CC BY-SA 2.0](https://creativecommons.org/licenses/by-sa/2.0/), https://www.flickr.com/photos/martinhipp/4212723686
* Sunset Cocktail, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/rodeime/16157402619
* Tango Berry Smoothie, [CC BY 2.0](https://creativecommons.org/licenses/by/2.0/), https://www.flickr.com/photos/vegateam/5867754540


## Changelog

* Version 1.0.0
	* Initial release
* Version 1.1.0
	* Update SDK and libraries
* Version 1.2.0
	* Runtime permissions
	* Support for database translations
	* One config file for everything (Google Analytics, AdMob)
	* Update SDK and libraries
	* Huge refactoring of the code with many improvements and optimizations
* Version 1.2.1
	* Fix loading data when activity is restored


## Developed by

[Robo Templates](http://robotemplates.com/)


## License

[Codecanyon license](http://codecanyon.net/licenses/standard)
