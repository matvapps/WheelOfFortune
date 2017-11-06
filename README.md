# WheelOfFortune

![library preview]()

Easy in use android library for creating wheel of fortune. With this library you
can add a wheel of fortune to your app at the few lines of code.

## Usage

### Create WheelOfFortune view

```xml
<com.github.matvapps.wheeloffortune.WOFView
        android:id="@+id/fortune_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

#### In activity
```java
wofView = (WOFView) findViewById(R.id.fortune_view);
```

#### Add new item
```java
wofView.addSector(R.drawable.wheel_of_fortune_image_1);
```
Or use full sector description:

```java
wofView.addSector(int fillColor,
                      int strokeColor,
                      float strokeSize,
                      int imageID));
```

#### Set rotation listener:
```java
wofView.setRotationListener(rotationListener);
```

### Full activity code:
```java
public class MainActivity extends AppCompatActivity {
  
    WOFView wofView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wofView = (WOFView) findViewById(R.id.fortune_view);

        for (int i = 0; i < 10; i++) {
            // add icon that will be used in sector
            wofView.addSector(R.drawable.ic_launcher);
        }

        wofView.setRotationListener(new CircleView.RotationListener() {
            @Override
            public void rotationStart() {

            }

            @Override
            public void rotationEnd(int sectorIndex) {
                // sectorIndex - index of sector winner
                // show index in toast
                Toast.makeText(MainActivity.this, sectorIndex + "", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
```


## Customize wheel


### Set color array (optional)

Create fortune colors resources:
```xml
    <color name="fortune_color_1">#7ad408</color>
    <color name="fortune_color_2">#00be3f</color>
    <color name="fortune_color_3">#11b9d6</color>
    <color name="fortune_color_4">#0948d9</color>
    <color name="fortune_color_5">#5207c8</color>
    <color name="fortune_color_6">#b201c5</color>
    <color name="fortune_color_7">#dc0039</color>
    <color name="fortune_color_8">#d90d0a</color>
    <color name="fortune_color_9">#ee6805</color>
    <color name="fortune_color_10">#fad006</color>
```

Create array from resources:
```xml
<array name="fortune_colors">

        <item>@color/fortune_color_1</item>
        <item>@color/fortune_color_2</item>
        <item>@color/fortune_color_3</item>
        <item>@color/fortune_color_4</item>
        <item>@color/fortune_color_5</item>
        <item>@color/fortune_color_6</item>
        <item>@color/fortune_color_7</item>
        <item>@color/fortune_color_8</item>
        <item>@color/fortune_color_9</item>
        <item>@color/fortune_color_10</item>

</array>
```

Set color array in code:
```java
wofView.setFillColors(getResources().getIntArray(R.array.fortune_colors);
```

### Set fortune image drawables (optional)

#### Set back drawable:
```java
wofView.setBackgroundImageID(R.drawable.fortune_back);
```

#### Set outer drawable:
```java
wofView.setOuterImageID(R.drawable.fortune_outer);
```

#### Set marker drawable: 
```java
wofView.setMarkerImageID(R.drawable.fortune_marker);
```

#### Or using XML:

```xml
app:wof_background="@drawable/fortune_back"
```

```xml
app:wof_outer="@drawable/fortune_outer"
```

```xml
app:wof_marker="@drawable/fortune_marker"
```

************


### Download

Download via Gradle:

```gradle
compile 'com.github.matvapps:wheeloffortune:0.0.1'
```
or Maven:
```xml
<dependency>
  <groupId>com.github.matvapps</groupId>
  <artifactId>WheelOfFortune</artifactId>
  <version>0.0.1</version>
  <type>pom</type>
</dependency>
```



Take a look at the [sample project](sample) for more information.

### License 

```
Copyright 2017 github.com/matvapps

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```


[sample]: <https://github.com/matvapps/WheelOfFortune/tree/master/sample>
