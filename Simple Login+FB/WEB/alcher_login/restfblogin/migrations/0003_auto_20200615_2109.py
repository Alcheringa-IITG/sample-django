# Generated by Django 3.0.7 on 2020-06-15 15:39

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('restfblogin', '0002_remove_user_author'),
    ]

    operations = [
        migrations.AlterField(
            model_name='user',
            name='email',
            field=models.CharField(default=0, max_length=400, unique=True),
        ),
        migrations.AlterField(
            model_name='user',
            name='fbid',
            field=models.CharField(default=0, max_length=400, unique=True),
        ),
    ]
