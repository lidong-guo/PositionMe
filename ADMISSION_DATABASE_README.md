# 河北省物理组大学专业录取分数线数据库 / Hebei Province Physics Group University Admission Score Database

## 概述 / Overview

这是一个用于存储和管理河北省物理组近三年大学专业录取分数线的数据库系统。该系统使用 Room 持久化库构建，提供了完整的数据访问接口。

This is a database system for storing and managing university major admission scores for the physics group in Hebei Province over the past three years. The system is built using the Room persistence library and provides complete data access interfaces.

## 数据库结构 / Database Structure

### 表格 / Tables

#### 1. universities (大学表)
存储大学基本信息 / Stores basic university information

| 字段 Field | 类型 Type | 说明 Description |
|------------|-----------|------------------|
| id | Long | 主键，自动生成 / Primary key, auto-generated |
| universityName | String | 大学名称 / University name |
| universityCode | String | 大学代码 / University code |
| province | String | 省份 / Province |
| city | String | 城市 / City |
| universityType | String | 大学类型 (985/211/双一流/普通) / University type |
| universityLevel | String | 批次 (本科一批/本科二批) / Admission batch |

#### 2. majors (专业表)
存储专业信息 / Stores major information

| 字段 Field | 类型 Type | 说明 Description |
|------------|-----------|------------------|
| id | Long | 主键，自动生成 / Primary key, auto-generated |
| universityId | Long | 外键，关联大学 / Foreign key to university |
| majorName | String | 专业名称 / Major name |
| majorCode | String | 专业代码 / Major code |
| majorCategory | String | 专业类别 (工学/理学/医学等) / Major category |
| degreeType | String | 学位类型 (本科/专科) / Degree type |
| duration | Integer | 学制年限 / Study duration in years |

#### 3. admission_scores (录取分数表)
存储录取分数线数据 / Stores admission score data

| 字段 Field | 类型 Type | 说明 Description |
|------------|-----------|------------------|
| id | Long | 主键，自动生成 / Primary key, auto-generated |
| universityId | Long | 外键，关联大学 / Foreign key to university |
| majorId | Long | 外键，关联专业 / Foreign key to major |
| year | Integer | 年份 (2022/2023/2024) / Year |
| province | String | 省份 (河北) / Province |
| subjectGroup | String | 选科组 (物理组) / Subject group |
| minimumScore | Integer | 最低分 / Minimum score |
| maximumScore | Integer | 最高分 / Maximum score |
| averageScore | Integer | 平均分 / Average score |
| rankMinimum | Integer | 最低位次 / Minimum rank |
| rankMaximum | Integer | 最高位次 / Maximum rank |
| plannedEnrollment | Integer | 计划招生人数 / Planned enrollment |
| actualEnrollment | Integer | 实际录取人数 / Actual enrollment |

## 使用方法 / Usage

### 1. 初始化数据库 / Initialize Database

```java
// Get database instance
AdmissionDatabase database = AdmissionDatabase.getInstance(context);

// Or use DatabaseHelper for easier operations
DatabaseHelper helper = new DatabaseHelper(context);
```

### 2. 插入示例数据 / Insert Sample Data

```java
DatabaseHelper helper = new DatabaseHelper(context);

helper.insertSampleData(new DatabaseHelper.DatabaseCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        Log.d(TAG, "Sample data inserted successfully");
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "Error inserting sample data", e);
    }
});
```

### 3. 查询数据 / Query Data

#### 查询所有大学 / Query All Universities
```java
new Thread(() -> {
    List<University> universities = database.universityDao().getAllUniversities();
    // Process universities
}).start();
```

#### 按年份查询录取分数 / Query Scores by Year
```java
helper.getAdmissionDetailsByYear(2024, "河北", "物理组", 
    new DatabaseHelper.DatabaseCallback<List<AdmissionDetail>>() {
        @Override
        public void onSuccess(List<AdmissionDetail> details) {
            for (AdmissionDetail detail : details) {
                Log.d(TAG, detail.toString());
            }
        }

        @Override
        public void onError(Exception e) {
            Log.e(TAG, "Error querying data", e);
        }
    });
```

#### 搜索专业 / Search Majors
```java
helper.searchMajors("计算机", new DatabaseHelper.DatabaseCallback<List<Major>>() {
    @Override
    public void onSuccess(List<Major> majors) {
        Log.d(TAG, "Found " + majors.size() + " majors");
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "Error searching", e);
    }
});
```

### 4. 获取数据库统计 / Get Database Statistics

```java
helper.getDatabaseStats(new DatabaseHelper.DatabaseCallback<String>() {
    @Override
    public void onSuccess(String stats) {
        Log.d(TAG, stats);
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "Error getting stats", e);
    }
});
```

## 示例数据 / Sample Data

数据库包含河北省主要大学的示例数据：
The database includes sample data for major universities in Hebei Province:

1. **河北大学** (Hebei University) - 保定
2. **燕山大学** (Yanshan University) - 秦皇岛
3. **河北工业大学** (Hebei University of Technology) - 天津 (211工程)
4. **华北电力大学(保定)** (North China Electric Power University) - 保定 (211工程)
5. **河北师范大学** (Hebei Normal University) - 石家庄
6. **河北医科大学** (Hebei Medical University) - 石家庄
7. **石家庄铁道大学** (Shijiazhuang Tiedao University) - 石家庄
8. **河北科技大学** (Hebei University of Science & Technology) - 石家庄
9. **河北农业大学** (Hebei Agricultural University) - 保定
10. **河北经贸大学** (Hebei University of Economics and Business) - 石家庄

### 专业类别 / Major Categories

每所大学包含常见的物理组相关专业：
Each university includes common physics-related majors:

- 计算机科学与技术 (Computer Science and Technology)
- 软件工程 (Software Engineering)
- 电子信息工程 (Electronic Information Engineering)
- 自动化 (Automation)
- 机械设计制造及其自动化 (Mechanical Design, Manufacturing and Automation)

### 时间范围 / Time Range

数据覆盖近三年 (2022-2024) 的录取分数线
Data covers admission scores for the past three years (2022-2024)

## DAO 接口 / DAO Interfaces

### UniversityDao
- `getAllUniversities()` - 获取所有大学 / Get all universities
- `getUniversityById(id)` - 按ID查询 / Query by ID
- `getUniversityByName(name)` - 按名称查询 / Query by name
- `getUniversitiesByProvince(province)` - 按省份查询 / Query by province
- `getUniversitiesByType(type)` - 按类型查询 / Query by type

### MajorDao
- `getAllMajors()` - 获取所有专业 / Get all majors
- `getMajorsByUniversity(universityId)` - 按大学查询专业 / Query majors by university
- `getMajorsByCategory(category)` - 按类别查询 / Query by category
- `searchMajorsByName(name)` - 搜索专业名称 / Search by name

### AdmissionScoreDao
- `getScoresByYear(year)` - 按年份查询 / Query by year
- `getScoresByProvinceAndSubject(province, subject)` - 按省份和选科查询 / Query by province and subject
- `getScoresByUniversityAndYear(universityId, year)` - 按大学和年份查询 / Query by university and year
- `getScoresByScoreRange(min, max)` - 按分数范围查询 / Query by score range
- `getAllYears()` - 获取所有年份 / Get all years

## 数据操作最佳实践 / Best Practices

1. **后台线程操作** / Background Thread Operations
   - 所有数据库操作应在后台线程执行
   - All database operations should be executed on background threads
   - 使用 `ExecutorService` 或 `AsyncTask`
   - Use `ExecutorService` or `AsyncTask`

2. **使用 DatabaseHelper** / Use DatabaseHelper
   - 使用 `DatabaseHelper` 类简化操作
   - Use `DatabaseHelper` class to simplify operations
   - 自动处理线程和回调
   - Automatically handles threading and callbacks

3. **资源清理** / Resource Cleanup
   - 完成操作后调用 `helper.shutdown()`
   - Call `helper.shutdown()` after completing operations
   - 使用 `AdmissionDatabase.closeDatabase()` 关闭数据库
   - Use `AdmissionDatabase.closeDatabase()` to close database

## 扩展功能 / Extension Features

### 添加新数据 / Add New Data

```java
// Add a new university
University newUniversity = new University(
    "新大学", "10999", "河北", "城市", "Regular", "本科一批"
);

new Thread(() -> {
    long id = database.universityDao().insert(newUniversity);
    Log.d(TAG, "Inserted university with ID: " + id);
}).start();
```

### 更新数据 / Update Data

```java
new Thread(() -> {
    University university = database.universityDao().getUniversityById(1);
    university.setUniversityType("211");
    database.universityDao().update(university);
}).start();
```

### 删除数据 / Delete Data

```java
helper.clearAllData(new DatabaseHelper.DatabaseCallback<Void>() {
    @Override
    public void onSuccess(Void result) {
        Log.d(TAG, "All data cleared");
    }

    @Override
    public void onError(Exception e) {
        Log.e(TAG, "Error clearing data", e);
    }
});
```

## 注意事项 / Important Notes

1. **权限要求** / Permission Requirements
   - 无需特殊权限，数据存储在应用私有目录
   - No special permissions required, data stored in app private directory

2. **数据迁移** / Data Migration
   - 当前使用 `.fallbackToDestructiveMigration()`
   - Currently using `.fallbackToDestructiveMigration()`
   - 升级数据库版本时会清空数据
   - Data will be cleared when upgrading database version

3. **性能优化** / Performance Optimization
   - 使用索引提高查询效率
   - Uses indexes to improve query performance
   - 批量操作使用 `insertAll()` 而非多次 `insert()`
   - Use `insertAll()` for batch operations instead of multiple `insert()`

## 技术栈 / Technology Stack

- **Room Database** 2.6.1 - Android 持久化库 / Android persistence library
- **SQLite** - 底层数据库引擎 / Underlying database engine
- **Java** - 编程语言 / Programming language
- **ExecutorService** - 异步操作 / Asynchronous operations

## 许可证 / License

This code is part of the PositionMe project.
