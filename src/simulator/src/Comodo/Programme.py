

class Programme():
    name = None
    category = []
    description = None
    dateStart = None
    dateStop = None

    def __init__(self, name, category, description, dateStart, dateStop):
        self.name = name
        self.category = category
        self.description = description
        self.dateStart = dateStart
        self.dateStop = dateStop

    def getName(self):
        return self.name

    def getCategory(self):
        return self.category

    def getDescription(self):
        return self.description

    def getDateStop(self):
        return self.dateStop

    def getDateStart(self):
        return self.dateStart
